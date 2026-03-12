package com.example.aws_lambda.controller;

import com.example.aws_lambda.entity.Order;
import com.example.aws_lambda.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/{userId}")
    public String create(@PathVariable String userId) {
        service.createOrder(userId);
        return "ok";
    }

    @GetMapping("/{userId}")
    public List<Order> list(@PathVariable String userId) {
        return service.findByUser(userId);
    }
}
