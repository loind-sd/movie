package com.example.aws_lambda.controller;

import com.example.aws_lambda.dto.UserCreateRequest;
import com.example.aws_lambda.entity.User;
import com.example.aws_lambda.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping()
    public String create(@RequestBody UserCreateRequest request) {
        userService.createUser(request.name(), request.age());
        return "ok";
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable String userId) {
        return userService.getUser(userId);
    }
}
