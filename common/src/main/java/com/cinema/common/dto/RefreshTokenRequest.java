package com.cinema.common.dto;

public record RefreshTokenRequest(Long userId, String refreshToken, Boolean callInsideService) {
}
