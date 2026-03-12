package com.cinema.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.RedisZSetCommands.Range;
import org.springframework.data.redis.connection.RedisZSetCommands.Limit;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrefixSearchSuggestionService {
    private final RedisService redisService;

    @Async
    public void recordSearch(String redisKey, String keyword) {
        if (keyword == null || keyword.length() < 2) return;

        keyword = keyword.toLowerCase();

        for (int i = 1; i <= keyword.length(); i++) {
            String prefix = keyword.substring(0, i);
            String value = prefix + "|" + keyword;

            log.info("Saving ZSET value = [{}]", value);

            redisService.incrementZSet(redisKey, value, 1);
        }
    }

    public Set<String> suggest(String redisKey, String prefix, int limit) {
        prefix = prefix.toLowerCase();

        Set<String> raw = redisService
                .reverseRangeByLex(
                        redisKey,
                        Range.range().gte(prefix + "|").lte(prefix + "|\uFFFF").toRange(),
                        Limit.limit().count(limit)
                );

        if (raw == null) return Set.of();

        return raw.stream()
                .map(v -> v.split("\\|")[1])
                .collect(Collectors.toSet());
    }
}
