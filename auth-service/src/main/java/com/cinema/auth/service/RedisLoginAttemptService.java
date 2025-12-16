package com.cinema.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisLoginAttemptService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final int MAX_ATTEMPT = 5;
    private static final long BLOCK_TIME_SECONDS = 10 * 60; // 10 phút

    private String attemptsKey(String username) {
        return "login:attempt:" + username;
    }

    private String blockKey(String username) {
        return "login:block:" + username;
    }

    /** Khi login thất bại */
    public void loginFailed(String username) {
        String key = attemptsKey(username);

        Long attempts = redisTemplate.opsForValue().increment(key);

        // set expire cho attempts nếu chưa có
        if (attempts == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(BLOCK_TIME_SECONDS));
        }

        if (attempts >= MAX_ATTEMPT) {
            redisTemplate.opsForValue().set(
                    blockKey(username),
                    "BLOCKED",
                    Duration.ofSeconds(BLOCK_TIME_SECONDS)
            );
        }
    }

    /** Khi login thành công */
    public void loginSucceeded(String username) {
        redisTemplate.delete(attemptsKey(username));
        redisTemplate.delete(blockKey(username));
    }

    /** Kiểm tra user có bị block không */
    public boolean isBlocked(String username) {
        return redisTemplate.hasKey(blockKey(username));
    }
}

