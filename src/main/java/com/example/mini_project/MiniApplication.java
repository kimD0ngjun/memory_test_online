package com.example.mini_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniApplication.class, args);
    }

}
