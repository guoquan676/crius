package com.pbkj.crius.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author GZQ
 */
@SpringBootApplication(scanBasePackages = {"com.pbkj.crius"})
@MapperScan(basePackages = {"com.pbkj.crius.*.mapper"})
public class Admin {
    public static void main(String[] args) {
        SpringApplication.run(Admin.class, args);
        System.out.println("hello world");
    }
}
