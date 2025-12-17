package com.cinema.security.support;

import com.cinema.security.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtClaimExtractor {

    private final JwtProperties properties;

    public Long extractUserId(Jwt jwt) {
        if ("sub".equals(properties.getUserIdClaim())) {
            return Long.parseLong(jwt.getSubject());
        }
        return jwt.getClaim(properties.getUserIdClaim());
    }
}
