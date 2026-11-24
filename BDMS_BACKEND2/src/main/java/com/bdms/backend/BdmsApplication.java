package com.bdms.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BdmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BdmsApplication.class, args);
    }
}
