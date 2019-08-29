package com.gadis.gadis.lib.job;

import com.gadis.gadis.lib.net.Msg;
import com.gadis.gadis.lib.net.MsgQueue;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class netWork implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(6379);

            while (true) {

                Socket socket = serverSocket.accept();
                try {
                    //获取输入流
                    InputStream inputStream = socket.getInputStream();

                    //获取请求内容，格式为，首行为请求的方法，之后为请求的参数 顺序为K,value,time,后两项可不传 全部由换行符分割
                    byte[] bytes = new byte[1024];

                    int length = inputStream.read(bytes);
                    String[] rqruestBody = new String(bytes, 0, length).split("\n");
                    Integer method = Integer.valueOf(rqruestBody[0]);

                    switch (method) {
                        //get
                        case 2:
                            //remove
                        case 3: {
                            Msg msg = new Msg(method, rqruestBody[1], socket);
                            MsgQueue.push(msg);
                            break;
                        }
                        //put
                        case 1: {
                            Msg msg = new Msg(method, rqruestBody[1], rqruestBody[2], Long.valueOf(rqruestBody[3]), socket);
                            MsgQueue.push(msg);
                            break;
                        }
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
