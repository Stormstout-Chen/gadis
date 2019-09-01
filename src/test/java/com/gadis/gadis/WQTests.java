package com.gadis.gadis;

import com.gadis.gadis.lib.net.Msg;
import com.gadis.gadis.lib.net.MsgQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.net.Socket;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WQTests {

    @Test
    public void testSet() throws Exception {

        Socket socket = new Socket("localhost",6379);

        OutputStream outputStream = socket.getOutputStream();

        File file = new File("src/test/msgTemplates/setRequest.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] bytes = new byte[1024];
        int requestLength = fileInputStream.read(bytes);

        String msg = new String(bytes);

        outputStream.write(bytes,0,requestLength);


    }

    @Test
    public void testGet() throws Exception {

        Socket socket = new Socket("localhost",6379);

        OutputStream outputStream = socket.getOutputStream();

        File file = new File("src/test/msgTemplates/getRequest.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

        byte[] bytes = new byte[1024];
        int requestLength = fileInputStream.read(bytes);

        outputStream.write(bytes,0,requestLength);

        InputStream inputStream = socket.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String msg = reader.readLine();

        System.out.println(msg);

    }

}
