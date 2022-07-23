package com.rest.ratelimitservice.util;

import com.google.inject.internal.util.ImmutableList;
import com.rest.ratelimitservice.annotation.RedisRateLimit;
import com.rest.ratelimitservice.enums.RateLimitType;
import com.rest.ratelimitservice.exception.RateLimitException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Configuration
public class RedisLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired(required = false)
    public RedisLimitAspect(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Around("@annotation(com.rest.ratelimitservice.annotation.RedisRateLimit)")
    public Object around(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        RedisRateLimit annotation = method.getAnnotation(RedisRateLimit.class);
        RateLimitType rateLimitType = annotation.limitType();

        String name = annotation.name();
        String key;

        int period = annotation.period();
        int count = annotation.count();

        switch (rateLimitType) {
            case IP:
                key = getIpAddress();
                break;
            case CUSTOMER:
                key = annotation.key();
                break;
            default:
                key = StringUtils.upperCase(method.getName());
        }
        ImmutableList<String> keys = ImmutableList.of(StringUtils.join(annotation.prefix(), key));
        try {
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/ratelimit.lua")));
            redisScript.setResultType(Long.class);

            Long res = redisTemplate.execute(redisScript, keys, count, period);
            log.info("Access try count is {} for name = {} and key = {}", res, name, key);
            if (res != null && res.intValue() == 1) {
                return pjp.proceed();
            } else throw new RateLimitException();
        } catch (Throwable e) {
            if (e instanceof RateLimitException) {
                log.debug("令牌桶={}，获取令牌失败", key);
                throw new RateLimitException();
            } else {
                e.printStackTrace();
                throw new RuntimeException("Server Error");
            }
        }
    }

    public String getIpAddress() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
