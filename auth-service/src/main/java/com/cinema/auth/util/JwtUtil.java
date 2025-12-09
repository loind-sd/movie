package com.cinema.auth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {


    private Key key;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration-ms}")
    private Long accessTokenExpirationMs;

    @Value("${test}")
    private String testValue;

    public void printTestValue() {
        System.out.println(testValue);  // Nên in ra "hello-world"
    }


//    public JwtUtil() {
//        printTestValue();
//        this.key = Keys.hmacShaKeyFor(secret.getBytes());
//    }

    public JwtUtil() {
        // Constructor trống vì không thể inject @Value vào constructor
        this.key = null;  // Khởi tạo mặc định
    }

    @PostConstruct
    public void init() {
        // Chỉ gọi sau khi Spring đã inject giá trị vào các field
        System.out.println("Test value: " + testValue);  // In ra "hello-world"
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }


    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenExpirationMs);


        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }
}
