package com.gadis.gadis.lib.job;

import com.gadis.gadis.lib.core.Cache;
import com.gadis.gadis.lib.net.Msg;
import com.gadis.gadis.lib.net.MsgQueue;

import java.io.IOException;
import java.io.OutputStream;

public class MsgOperator implements Runnable {
    @Override
    public void run() {

        while (true) {

            //无消息则等待

                if (MsgQueue.isEmpty()) {
                    try {
                        synchronized (MsgOperator.class) {
                            MsgOperator.class.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }

            operatorMsg(MsgQueue.poll());

        }

    }

    /**
     * 处理消息的方法
     * 结合NetWork gadis的业务处理和网络交互 都是单线程的
     */
    private void operatorMsg(Msg msg) {

        switch (msg.getMethod()) {

            case 1: {

                Cache.set(msg.getK(), msg.getValue(), msg.getOutTime());
                break;

            }
            case 2: {

                String value = Cache.get(msg.getK());
                if (value == null){
                    value = "no such key";
                }

                OutputStream outputStream = null;
                try {
                    //做响应
                    outputStream = msg.getSocket().getOutputStream();
                    outputStream.write(value.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //释放资源
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        msg.getSocket().close();
                    } catch (IOException e) {
                    }
                }

                break;
            }
            case 3: {
                Cache.remove(msg.getK());
                break;
            }

            default:
                break;
        }

    }

}
