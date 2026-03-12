package com.cinema.showtimeservice.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RepositoryMonitoringAspect {

    private final MeterRegistry meterRegistry;

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object monitor(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();
        String repository = joinPoint.getSignature()
                .getDeclaringType()
                .getSimpleName();
        String method = joinPoint.getSignature().getName();

        try {
            return joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;

            Timer.builder("jpa.repository.execution")
                    .tag("repository", repository)
                    .tag("method", method)
                    .register(meterRegistry)
                    .record(duration, TimeUnit.MILLISECONDS);

            if (duration > 1000) {
                Counter.builder("jpa.repository.slow")
                        .tag("repository", repository)
                        .tag("method", method)
                        .register(meterRegistry)
                        .increment();
            }
        }
    }
}
