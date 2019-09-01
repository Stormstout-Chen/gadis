package com.gadis.gadis;

import com.gadis.gadis.lib.job.MsgOperator;
import com.gadis.gadis.lib.job.NetWork;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

/**
 * @author Stormstout-Chen
 */
@SpringBootApplication
@EnableScheduling
public class GadisApplication {

    public static void main(String[] args) {

        SpringApplication.run(GadisApplication.class, args);

        new Thread(new NetWork()).start();
        new Thread(new MsgOperator()).start();
    }

}
