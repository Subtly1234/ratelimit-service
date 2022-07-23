package com.rest.ratelimitservice.controller;

import com.rest.ratelimitservice.annotation.RedisRateLimit;
import com.rest.ratelimitservice.dto.HelloDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;
import java.lang.reflect.Method;

@Slf4j
@RestController
public class RateLimitController {
    @RedisRateLimit(count = 1, period = 2)
    @GetMapping("/rateLimitRestApi")
    public HelloDTO rateLimitRestApi() {
        return new HelloDTO();
    }
}