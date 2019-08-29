package com.gadis.gadis.lib.core;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    //缓存容器
    private static final ConcurrentHashMap<String, CacheObj> cacheMap = new ConcurrentHashMap();

    //最大容量
    private static Integer MAX_SIZE = 10000;

    //操作日志，用于清理内存
    private static List<String> operationLog = Collections.synchronizedList(new ArrayList<String>());

    //是否正在做缓存淘汰
    private static volatile Boolean is_weeking_out = false;

    public static Boolean set(String k, Object value, Long time) {

        if (time != null && time <= 0) {
            return false;
        }

        Long outTime = time == null ? null : System.currentTimeMillis() + time;

        CacheObj cacheObj = new CacheObj(value, outTime);

        cacheMap.put(k, cacheObj);

        operationLog.removeAll(Collections.singleton(k));
        operationLog.add(k);

        if (getSize() > MAX_SIZE + (MAX_SIZE >> 1) && !is_weeking_out) {
            weedOut();
        }

        return true;
    }

    public static Object get(String k) {

        operationLog.removeAll(Collections.singleton(k));
        operationLog.add(k);

        CacheObj cacheObj = cacheMap.get(k);

        if (cacheObj == null || cacheObj.getOutTime() != null && cacheObj.getOutTime() < System.currentTimeMillis()){
            return null;
        }

        return cacheObj.getValue();

    }

    public static void remove(String k) {
        cacheMap.remove(k);
    }

    public static void flush() {
        operationLog.clear();
        cacheMap.clear();
    }

    /**
     * 清理过期缓存，每分钟跑一次
     */
    @Scheduled(cron = "0/1 0/1 * * * ?")
    public void retrieve() {
        ArrayList<String> ksToRemove = new ArrayList<>(1000);

        for (Map.Entry<String, CacheObj> cacheObj : cacheMap.entrySet()) {

            if (cacheObj.getValue().getOutTime() != null && cacheObj.getValue().getOutTime() < System.currentTimeMillis()) {
                ksToRemove.add(cacheObj.getKey());
            }

        }

        for (String k : ksToRemove) {
            remove(k);
        }

    }

    public static Integer getSize() {
        return cacheMap.size();
    }

    //缓存淘汰——优先淘汰最久未使用的缓存
    private synchronized static void weedOut() {

        if (is_weeking_out) {
            return;
        }

        if (getSize() <= MAX_SIZE + (MAX_SIZE >> 1)) {
            is_weeking_out = false;
            return;
        }

        is_weeking_out = true;

        Iterator<String> iterator = new ArrayList<>(operationLog).iterator();

        String k;
        while (iterator.hasNext()) {

            if (getSize() <= MAX_SIZE) {
                break;
            }

            k = iterator.next();
            remove(k);
            operationLog.remove(k);

        }

        is_weeking_out = false;

    }

}
