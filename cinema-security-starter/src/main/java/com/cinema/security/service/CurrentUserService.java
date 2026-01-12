package com.cinema.security.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

public class CurrentUserService {
    public static CurrentUserService INSTANCE = new CurrentUserService();

    public Long getCurrentUserLogin() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return Long.valueOf(((JwtAuthenticationToken) auth).getToken().getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
