package com.gadis.gadis.lib;

//一条缓存
public class CacheObj {

    //内容
    private Object value;
    //过期时间
    private Long outTime;

    public CacheObj() {
    }
    public CacheObj(Object value, Long outTime) {
        this.value = value;
        this.outTime = outTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }
}
