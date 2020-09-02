package com.pbkj.crius.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author GZQ
 */
@SpringBootApplication(scanBasePackages = {"com.pbkj.crius"})
@MapperScan(basePackages = {"com.pbkj.crius.mapper"})
@EnableDiscoveryClient
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("hello world");
    }
}
