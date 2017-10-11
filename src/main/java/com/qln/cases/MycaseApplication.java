package com.qln.cases;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.qln.cases.mq" })
@MapperScan("com.qln.cases.shiro.dao")
public class MycaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MycaseApplication.class, args);
    }
}
