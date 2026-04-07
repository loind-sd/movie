package com.cinema.cinemaservice.aop;

import com.cinema.common.exception.BadRequestException;
import com.cinema.common.exception.ErrorCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Aspect
@Component
public class RateLimitAspect {

    // key -> [count, windowStart]
    private final Map<String, long[]> buckets =
            new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object handle(ProceedingJoinPoint pjp, RateLimit rateLimit)
            throws Throwable {

        String key = rateLimit.key() + ":" + pjp.getSignature().getName();
        long windowMs = rateLimit.unit().toMillis(rateLimit.duration());
        long now = System.currentTimeMillis();

        buckets.merge(key, new long[]{1, now}, (old, init) -> {
            if (now - old[1] > windowMs) return new long[]{1, now};
            old[0]++;
            return old;
        });

        long[] bucket = buckets.get(key);
        if (bucket[0] > rateLimit.maxRequests()) {
            throw new BadRequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return pjp.proceed();
    }
}
