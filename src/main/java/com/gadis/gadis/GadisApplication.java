package com.gadis.gadis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GadisApplication {

    public static void main(String[] args) {
        SpringApplication.run(GadisApplication.class, args);
    }

}
