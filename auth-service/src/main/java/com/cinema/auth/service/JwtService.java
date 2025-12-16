package com.cinema.auth.service;

import com.cinema.auth.config.JwtKeyProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtKeyProvider keyProvider;

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.access-token-expire}")
    private Long accessTokenExpire;
    @Value("${jwt.refresh-token-expire}")
    private Long refreshTokenExpire;

    public String generateToken(
            String userId
    ) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(userId)
                .setIssuer(issuer)
//                .claim("username", username)
//                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(accessTokenExpire, ChronoUnit.MILLIS)))
                .signWith(keyProvider.getPrivateKey(), SignatureAlgorithm.RS256)
                .compact();
    }
}

