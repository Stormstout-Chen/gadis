package com.gadis.gadis;

import com.gadis.gadis.lib.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GadisApplicationTests {

    @Test
    public void contextLoads() throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 10000; i++) {
                    Cache.set(i + "", "wq", null);
                }
                System.out.println("1跑完");

            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < 22006; i++) {
                    Cache.set(i + "", "wq", null);
                }
                System.out.println("2跑完");
            }
        });

        long l1 = System.currentTimeMillis();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        long l2 = System.currentTimeMillis();
        System.out.println(l2-l1);
        System.out.println(Cache.getSize());
    }

}
