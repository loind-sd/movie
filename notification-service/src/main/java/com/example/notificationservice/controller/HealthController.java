package com.example.notificationservice.controller;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/health")
public class HealthController {
 @GetMapping public String ok(){return "ok";}}
