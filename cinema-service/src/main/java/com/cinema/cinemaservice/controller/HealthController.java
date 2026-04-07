package com.cinema.cinemaservice.controller;

import com.cinema.cinemaservice.aop.RateLimit;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    @RateLimit(maxRequests = 5, duration = 1, unit = TimeUnit.MINUTES, key = "login")
    public String ok() {
        return "ok";
    }
}
