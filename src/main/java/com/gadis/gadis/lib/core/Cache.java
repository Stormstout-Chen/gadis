package com.gadis.gadis.lib.core;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Stormstout-Chen
 * 核心业务类
 */
public class Cache {

    /**
     * 缓存容器
     */
    private static final ConcurrentHashMap<String, CacheObj> CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 最大容量
     */
    private static Integer MAX_SIZE = 10000;

    /**
     * 操作日志，用于清理内存
     */
    private static List<String> operationLog = Collections.synchronizedList(new ArrayList<>());

    /**
     * 是否正在做缓存淘汰
     */
    private static volatile Boolean is_weeding_out = false;

    public synchronized static Boolean set(String k, String value, Long time) {

        if (time != null && time <= 0) {
            return false;
        }

        Long outTime = time == null ? null : System.currentTimeMillis() + time;

        CacheObj cacheObj = new CacheObj(value, outTime);

        CACHE_MAP.put(k, cacheObj);

        operationLog.removeAll(Collections.singleton(k));
        operationLog.add(k);

        if (getSize() > MAX_SIZE + (MAX_SIZE >> 1)) {
            weedOut();
        }

        return true;
    }

    /**
     * TODO 方法报错了
     *
     * @param k key
     * @return Value
     */
    public static String get(String k) {

        operationLog.removeAll(Collections.singleton(k));
        operationLog.add(k);

        CacheObj cacheObj = CACHE_MAP.get(k);

        if (cacheObj == null || cacheObj.getOutTime() != null && cacheObj.getOutTime() < System.currentTimeMillis()) {
            return null;
        }

        return cacheObj.getValue();

    }

    public static void remove(String k) {
        CACHE_MAP.remove(k);
    }

    public static void flush() {
        operationLog.clear();
        CACHE_MAP.clear();
    }

    /**
     * 清理过期缓存，每分钟跑一次
     * TODO 此方法不执行 执行的，只不过定时任务的线程绑定不到控制台上 O(∩_∩)O
     */
    @Scheduled(cron = "0/1 0/1 * * * ?")
    public void retrieve() {

        //做淘汰时不能set
        synchronized (Cache.class){

            ArrayList<String> ksToRemove = new ArrayList<>(1000);

            for (Map.Entry<String, CacheObj> cacheObj : CACHE_MAP.entrySet()) {

                if (cacheObj.getValue().getOutTime() != null && cacheObj.getValue().getOutTime() < System.currentTimeMillis()) {
                    ksToRemove.add(cacheObj.getKey());
                }

            }

            for (String k : ksToRemove) {
                remove(k);
            }
        }

    }

    public static Integer getSize() {
        return CACHE_MAP.size();
    }

    /**
     * 缓存淘汰——优先淘汰最久未使用的缓存
     */
    private synchronized static void weedOut() {

        Iterator<String> iterator = new ArrayList<>(operationLog).iterator();

        String k;
        while (iterator.hasNext()) {

            if (getSize() <= MAX_SIZE) {
                break;
            }

            k = iterator.next();
            remove(k);
            iterator.remove();

        }

    }

    public static ConcurrentHashMap<String, CacheObj> getBackUp() {
        return CACHE_MAP;
    }

    public static void synchronizationBackUp(ConcurrentHashMap<String, CacheObj> map) {
        CACHE_MAP.clear();
        CACHE_MAP.putAll(map);
    }
}
