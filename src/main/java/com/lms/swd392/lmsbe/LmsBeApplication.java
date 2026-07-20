package com.lms.swd392.lmsbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LmsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LmsBeApplication.class, args);
    }

}
