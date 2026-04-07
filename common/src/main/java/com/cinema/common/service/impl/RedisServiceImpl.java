package com.cinema.common.service.impl;

import com.cinema.common.service.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.Limit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;
    private final RedisTemplate<String, String> stringRedisTemplate;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate,
                            HashOperations<String, String, Object> hashOperations,
                            @Qualifier("stringRedisTemplatess")
                            RedisTemplate<String, String> stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = hashOperations;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @PostConstruct
    void logSerializer() {
        log.info("Redis value serializer = {}",
                stringRedisTemplate.getValueSerializer().getClass().getName());
    }

    @Override
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setValueWithExpireTime(String key, Object value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    @Override
    public boolean setValueWithExpireTimeIfAbsent(String key, Object value, long expireTime, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, timeUnit);
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

    @Override
    public void incrementZSet(String key, String member, double delta) {
        stringRedisTemplate.opsForZSet().incrementScore(key, member, delta);
    }

    @Override
    public Set<String> reverseRangeByLex(String key, Range<String> range, Limit limit) {
        return stringRedisTemplate.opsForZSet().reverseRangeByLex(key, range, limit);
    }

    @Override
    public void keepZSetTopN(String key, int topN) {
        Long size = stringRedisTemplate.opsForZSet().size(key);
        if (size == null || size <= topN) {
            return;
        }

        long end = size - topN - 1;

        stringRedisTemplate.opsForZSet()
                .removeRange(key, 0, end);
    }

    @Override
    public <T> T checkExistAndPerform(String key, Function<String, T> action, Class<T> type) {
        Object data = getValue(key);
        if (data != null) {
            return type.cast(data);
        }
        int retryCount = 1;
        while (retryCount++ <= 3) {
            boolean lockAcquired = tryLock(key + ":lock", 10, TimeUnit.SECONDS);
            if (lockAcquired) {
                Object result = getValue(key) ;
                if (result != null) {
                    return type.cast(result);
                }
                return action.apply(key);
            } else {
                try {
                    Thread.sleep(100); // Wait briefly before retrying
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                // check đã có cache hay chưa sau khi chờ
                Object result = getValue(key);
                if (result != null) {
                    return type.cast(result);
                }
            }
        }

        return action.apply(key);
    }

    private boolean tryLock(String key, long expireTime, TimeUnit timeUnit) {
        try {
            return setValueWithExpireTimeIfAbsent(key, "LOCKED", expireTime, timeUnit);
        } catch (Exception e) {
            log.error("Failed to acquire lock for key: {}", key, e);
            return false;
        }
    }


}
