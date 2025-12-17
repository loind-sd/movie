package com.cinema.common.auto_config;

import com.cinema.common.service.impl.RedisServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@AutoConfiguration
public class RedisAutoConfiguration {

    /**
     * Tạo bean RedisService nếu chưa có bean nào cùng loại được định nghĩa trong ngữ cảnh ứng dụng.
     * Sử dụng RedisTemplate và HashOperations để khởi tạo RedisService.
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisServiceImpl redisService(RedisTemplate<String, Object> redisTemplate, HashOperations<String, String, Object> hashOperations) {
        return new RedisServiceImpl(redisTemplate, hashOperations) {};
    }
}
