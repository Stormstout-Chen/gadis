package com.gadis.gadis.lib.core;

import com.alibaba.fastjson.JSON;

/**
 * @author Stormstout-Chen
 * 一条缓存
 */
public class CacheObj {

    /**
     * 缓存内容 格式为json字符串
     */
    private String value;

    /**
     * 缓存过期时间
     */
    private Long outTime;


    /**
     * C and G T
     */
    public CacheObj() {
    }

    public CacheObj(String value, Long outTime) {
        this.value = value;
        this.outTime = outTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = JSON.toJSONString(value);
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }
}
