package com.jimmy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan("com.jimmy.mapper")
@SpringBootApplication
public class HanataApplication {

    public static void main(String[] args) {
        SpringApplication.run(HanataApplication.class, args);
    }

}
