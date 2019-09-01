package com.gadis.gadis.lib.net;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MsgQueue {

    /**
     * 待处理的消息队列
     * 供唯一的处理线程逐一消费
    * */
    public static ConcurrentLinkedQueue<Msg> queue = new ConcurrentLinkedQueue();

    public static void push(Msg msg){
        queue.add(msg);
    }

    public static Msg poll(){
        return queue.poll();
    }

    public static Integer getSize(){
        return queue.size();
    }

    public static Boolean isEmpty(){
        return queue.isEmpty();
    }
}
