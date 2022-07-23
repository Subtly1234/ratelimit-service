package com.rest.ratelimitservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RateLimitServiceApplication {

    public static void main(String[] args) {
        String ip = System.getenv("MY_NODE_NAME");
        System.err.println(ip);
        SpringApplication.run(RateLimitServiceApplication.class, args);
    }

}
