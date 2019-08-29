package com.gadis.gadis.lib.net;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MsgQueue {

    /**
     * 待处理的消息队列
     * 供唯一的处理线程逐一消费
    * */
    private static ConcurrentLinkedQueue<Msg> queue = new ConcurrentLinkedQueue();

    public static void push(Msg msg){
        queue.add(msg);
    }

    public static Msg poll(Msg msg){
        return queue.poll();
    }
}
