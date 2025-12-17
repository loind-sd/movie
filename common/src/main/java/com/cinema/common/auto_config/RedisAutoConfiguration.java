package com.cinema.common.auto_config;

import com.cinema.common.service.impl.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@AutoConfiguration
public class RedisAutoConfiguration {

    /**
     * Tạo bean RedisTemplate nếu chưa có bean nào cùng loại được định nghĩa trong ngữ cảnh ứng dụng.
     * Sử dụng RedisConnectionFactory và ObjectMapper để cấu hình RedisTemplate.
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory,
            @Qualifier("commonObjectMapper") ObjectMapper objectMapper
    ) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<Object> serializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Tạo bean HashOperations nếu chưa có bean nào cùng loại được định nghĩa trong ngữ cảnh ứng dụng.
     * Sử dụng RedisTemplate để khởi tạo HashOperations.
     */
    @Bean
    @ConditionalOnMissingBean
    public HashOperations<String, String, Object> hashOperations(
            RedisTemplate<String, Object> redisTemplate
    ) {
        return redisTemplate.opsForHash();
    }

    /**
     * Tạo bean RedisService nếu chưa có bean nào cùng loại được định nghĩa trong ngữ cảnh ứng dụng.
     * Sử dụng RedisTemplate và HashOperations để khởi tạo RedisService.
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisServiceImpl redisService(RedisTemplate<String, Object> redisTemplate, HashOperations<String, String, Object> hashOperations) {
        return new RedisServiceImpl(redisTemplate, hashOperations) {
        };
    }
}
