package com.gadis.gadis.lib.persistence;

import com.alibaba.fastjson.JSON;
import com.gadis.gadis.lib.core.Cache;
import com.gadis.gadis.lib.core.CacheObj;
import okio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GitDatSanvich
 * @version BackUp 0.0.01
 * @date 2019/8/24 9:43
 **/
@Service
class BackUp {
    private static Logger logger = LoggerFactory.getLogger(BackUp.class);
    /**
     * 存储位置
     * 确认自己有盘没有F盘可能会报错
     */
    @Value("${back-up.storage}")
    private String backUpStorage;

    /**
     * 缓存持久化
     * 每10分钟持久化一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    private void cachePersistence() {
        logger.info("执行持久化");
        ConcurrentHashMap<String, CacheObj> backUp = Cache.getBackUp();
        //备份为空不备份
        if (backUp == null || backUp.keySet().size() == 0) {
            return;
        }
        //持久化文件
        logger.info("持久化文件");
        String decodeString = JSON.toJSONString(backUp);
        byte[] decodeBytes = decodeString.getBytes();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encode = encoder.encode(decodeBytes);
        //写入文件
        File backUpDir = this.persistence(encode);
        //写入成功 删除超时文件
        deleteBackUpFile(backUpDir);
    }

    private void deleteBackUpFile(File backUpDir) {
        if (backUpDir.isDirectory()) {
            String[] backUpFiles = backUpDir.list();
            if (backUpFiles == null) {
                throw new RuntimeException("检查备份文件错误");
            }
            List<Long> timeList = new ArrayList<>();
            if (backUpFiles.length > 5) {
                for (String fileName : backUpFiles) {
                    if (fileName.endsWith(".cache")) {
                        long time = Long.parseLong(fileName.substring(0, fileName.indexOf('.')));
                        timeList.add(time);
                    }
                }
            }
            if (timeList.size() < 5) {
                return;
            }
            Collections.sort(timeList);
            List<Long> deleteList = timeList.subList(0, timeList.size() - 5);
            for (Long delete : deleteList) {
                String name = delete.toString();
                for (String backUpFile : backUpFiles) {
                    if (backUpFile.contains(name)) {
                        File deleteFile = new File(backUpStorage + "\\" + backUpFile);
                        boolean d = deleteFile.delete();
                        if (!d) {
                            //删除失败
                            logger.error("无法删除备份文件" + backUpFile);
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("备份文件路径错误 请检查写入路径");
        }
    }

    private File persistence(byte[] encode) {
        File backUpDir = new File(backUpStorage);
        try {
            //保存路径
            //路径不存在创建文件夹
            if (!backUpDir.exists()) {
                boolean mkdirs = backUpDir.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("无法创建文件夹请确认配置是否正确");
                }
            }
            File backUpFile = new File(backUpStorage + "\\" + System.currentTimeMillis() + ".cache");
            Sink sink = Okio.sink(backUpFile);
            BufferedSink bufferedSink = Okio.buffer(sink);
            bufferedSink.write(encode);
            bufferedSink.flush();
            bufferedSink.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入失败");
        }
        return backUpDir;
    }

    @PostConstruct
    public void readBackUp() {
        try {
            logger.info("读取持久化文件");
            //获得最后一次持久化文件
            File backUpDir = new File(backUpStorage);
            if (!backUpDir.exists()) {
                return;
            } else if (backUpDir.list() == null || backUpDir.list().length == 0) {
                return;
            }
            String[] files = backUpDir.list();
            if (files == null) {
                return;
            }
            byte[] encodeByte = null;
            try {
                encodeByte = readCacheFile(files);
            } catch (IOException e) {
                e.printStackTrace();
                //文件读取错误
                logger.info("文件读取失败!");
                throw new RuntimeException();
            }
            if (encodeByte == null) {
                logger.info("读取失败");
                throw new RuntimeException();
            }
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(encodeByte);
            String json = new String(decode);
            ConcurrentHashMap<String, CacheObj> concurrentHashMap = JSON.parseObject(json, ConcurrentHashMap.class);
            Cache.synchronizationBackUp(concurrentHashMap);
        } catch (RuntimeException e) {
            logger.info(e.getMessage());
        }
    }

    private byte[] readCacheFile(String[] files) throws IOException {
        List<Long> timeList = new ArrayList<>();
        for (String fileName : files) {
            if (fileName.endsWith(".cache")) {
                long time = Long.parseLong(fileName.substring(0, fileName.indexOf('.')));
                timeList.add(time);
            }
        }
        if (timeList.size() == 0) {
            throw new RuntimeException("无cache文件存储");
        }
        Collections.sort(timeList);
        Long readTime = timeList.get(timeList.size() - 1);
        String backUpPath = backUpStorage + "\\" + readTime + ".cache";
        File backUp = new File(backUpPath);
        Source source = Okio.source(backUp);
        BufferedSource buffer = Okio.buffer(source);
        return buffer.readByteArray();
    }
}
