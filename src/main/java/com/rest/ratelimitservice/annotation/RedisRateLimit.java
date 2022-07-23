package com.rest.ratelimitservice.annotation;
import com.rest.ratelimitservice.enums.RateLimitType;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisRateLimit {
    String name() default "";

    String key() default "";

    String prefix() default "";

    int period() default 1;

    int count() default 100;

    RateLimitType limitType() default RateLimitType.DEFAULT;
}