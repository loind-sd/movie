package com.cinema.auth.dto;

public record JwtResponse(Long userId, String accessToken, String refreshToken, String tokenType) {
}
