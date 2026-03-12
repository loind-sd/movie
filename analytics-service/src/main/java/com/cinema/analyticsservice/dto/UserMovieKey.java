package com.cinema.analyticsservice.dto;

public record UserMovieKey(
        String userId,
        String movieId
) {}