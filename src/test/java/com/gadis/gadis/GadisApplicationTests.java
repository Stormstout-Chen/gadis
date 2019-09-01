package com.gadis.gadis;

import com.gadis.gadis.lib.core.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentLinkedQueue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GadisApplicationTests {

    @Test
    public void contextLoads() throws InterruptedException {

        Thread.sleep(10000);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 100000; i++) {
                    Cache.set(i + "", "wq", null);
                }
                System.out.println("1跑完");

            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 100000; i < 220006; i++) {
                    Cache.set(i + "", "wq", null);
                }
                System.out.println("2跑完");
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 220006; i++) {
                    System.out.println(Cache.get(i+""));
                }
                System.out.println("3跑完");
            }
        });

        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 220006; i++) {
                    Cache.remove(i+"");
                }
                System.out.println("3跑完");
            }
        });

        long l1 = System.currentTimeMillis();
        t1.start();
        t2.start();
//        t3.start();
//        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        long l2 = System.currentTimeMillis();
        System.out.println(l2-l1);
        System.out.println(Cache.getSize());
    }

    @Test
    public void t2(){
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue();
        Object poll = queue.poll();
        System.out.println(poll);
        System.out.println(666);
    }

}
