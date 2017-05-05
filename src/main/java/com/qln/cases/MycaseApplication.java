package com.qln.cases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.qln.cases" })
public class MycaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycaseApplication.class, args);
    }
}
