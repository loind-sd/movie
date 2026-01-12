package com.cinema.common.service.impl;

import com.cinema.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValueWithExpireTime(String key, Object value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    @Override
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void removeValue(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public Set<String> findKeysWithPrefix(String prefix) {
        String pattern = prefix + "*"; // Ví dụ: "SHOWTIME_*"
        return redisTemplate.keys(pattern);
    }

    @Override
    public void putToHash(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public void deleteFromHash(String key, String field) {
        hashOperations.delete(key, field);
    }

    @Override
    public Object getFromHash(String key, String field) {
        return hashOperations.get(key, field);
    }

    @Override
    public Map<String, Object> getAllInHash(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Long incrementHash(String key, String field) {
        return hashOperations.increment(key, field, 1);
    }

    @Override
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
