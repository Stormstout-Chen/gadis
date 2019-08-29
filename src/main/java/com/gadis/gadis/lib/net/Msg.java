package com.gadis.gadis.lib.net;

import java.net.Socket;

/**
 * 消息类
 * 封装一个请求的全部内容
 * 存入MsgQueue中，供消费线程逐一使用
* */
public class Msg {

    //请求执行的方法
    private Integer method;

    //key值
    private String k;

    //数据内容
    private String value;

    //过期时间
    private Long outTime;

    //此消息对应的网路连接
    private Socket socket;

    /**
    * C and G T
    * */
    public Msg() {
    }

    public Msg(Integer method, String k,Socket socket) {
        this.method = method;
        this.k = k;
        this.socket = socket;
    }

    public Msg(Integer method, String k, String value, Long outTime, Socket socket) {
        this.method = method;
        this.k = k;
        this.value = value;
        this.outTime = outTime;
        this.socket = socket;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOutTime() {
        return outTime;
    }

    public void setOutTime(Long outTime) {
        this.outTime = outTime;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
