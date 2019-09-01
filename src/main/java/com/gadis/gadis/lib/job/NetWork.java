package com.gadis.gadis.lib.job;

import com.gadis.gadis.lib.net.Msg;
import com.gadis.gadis.lib.net.MsgQueue;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class NetWork implements Runnable {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(6379);

            while (true) {
                //sout
                System.out.println("Quene Size:"+MsgQueue.getSize());
                System.out.println(MsgQueue.queue);

                Socket socket = serverSocket.accept();
                try {
                    //获取输入流
                    InputStream inputStream = socket.getInputStream();

                    //获取请求内容，格式为，首行为请求的方法，之后为请求的参数 顺序为K,value,time,后两项可不传 全部由换行符分割
                    byte[] bytes = new byte[1024];

                    int length = inputStream.read(bytes);

                    String[] requestBody = new String(bytes, 0, length).split("\r\n");

                    //sout
                    System.out.println(">>>>>>>>>>>>>>>>>>>>");
                    System.out.println(new String(bytes));
                    System.out.println(Arrays.toString(requestBody));
                    System.out.println(">>>>>>>>>>>>>>>>>>>>");

                    Integer method = Integer.valueOf(requestBody[0]);

                    switch (method) {
                        //put
                        case 1: {

                            Long outTime = requestBody.length == 4 ? Long.valueOf(requestBody[3]) : null;

                            Msg msg = new Msg(method, requestBody[1], requestBody[2], outTime , socket);
                            MsgQueue.push(msg);
                            synchronized (MsgOperator.class){
                                MsgOperator.class.notifyAll();
                            }
                            break;
                        }
                        //get
                        case 2:
                        //remove
                        case 3: {
                            Msg msg = new Msg(method, requestBody[1], socket);
                            MsgQueue.push(msg);
                            synchronized (MsgOperator.class){
                                MsgOperator.class.notifyAll();
                            }
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
