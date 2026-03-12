package com.example.aws_lambda.service;

import com.example.aws_lambda.entity.User;
import com.example.aws_lambda.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String createUser(String name, int age) {
        var user = new com.example.aws_lambda.entity.User();
        user.setName(name);
        user.setAge(age);
        var saved = userRepository.save(user);
        log.info("User created: {}", saved);
        return saved.getId();
    }

    public User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }
}
