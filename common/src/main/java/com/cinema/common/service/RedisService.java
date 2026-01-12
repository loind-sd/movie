package com.cinema.common.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    void setValue(String key, Object value);
    void setValueWithExpireTime(String key, Object value, long expireTime, TimeUnit timeUnit);
    Object getValue(String key);
    void removeValue(String key);
    Set<String> findKeysWithPrefix(String prefix);

    void putToHash(String key, String field, Object value);
    void deleteFromHash(String key, String field);
    Object getFromHash(String key, String field);
    Map<String, Object> getAllInHash(String key);
    Long incrementHash(String key, String field);

    void convertAndSend(String channel, Object message);
}
