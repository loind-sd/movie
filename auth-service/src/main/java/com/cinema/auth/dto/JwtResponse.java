package com.cinema.auth.dto;

public record JwtResponse(String accessToken, String refreshToken, String tokenType) {
}
