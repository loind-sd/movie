package com.cinema.showtimeservice.service;

import com.cinema.common.constants.CommonConstants;
import com.cinema.common.dto.RefreshTokenRequest;
import com.cinema.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenService {
    private final RestTemplate restTemplate;
    private final RedisService redisService;

    public String getUserToken(Long userId) {
        String tokenKey = String.format(CommonConstants.RedisKey.TOKEN_KEY, userId);
        Object data = redisService.getValue(tokenKey);

        // có token của user này
        if (data != null) {
            return String.valueOf(data);
        }

        // không có token của user này, gọi auth service để lấy
        try {
            RefreshTokenRequest request = new RefreshTokenRequest(userId, "", true);
            String url = "http://localhost:8080/api/auth/refresh"; // URL của auth service
            restTemplate.postForObject(url, request, Object.class);
            data = redisService.getValue(tokenKey);
        } catch (Exception e) {
            log.error("Failed to fetch token from auth service for userId {}: {}", userId, e.getMessage());
        }

        return String.valueOf(data);
    }
}
