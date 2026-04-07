package com.cinema.cinemaservice.aop;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    int maxRequests() default 10;         // số request tối đa
    long duration() default 1;            // khoảng thời gian
    TimeUnit unit() default TimeUnit.MINUTES;
    String key() default "default";       // tên bucket riêng
}
