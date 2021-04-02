package com.pbkj.crius.redisjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author GZQ
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.pbkj.crius")
public class RedisJob {
    public static void main(String[] args) {
        SpringApplication.run(RedisJob.class, args);
        System.out.println("hello world");
    }
}
