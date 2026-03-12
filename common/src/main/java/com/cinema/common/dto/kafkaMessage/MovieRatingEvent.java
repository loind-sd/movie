package com.cinema.common.dto.kafkaMessage;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MovieRatingEvent {
    private String userId;
    private String movieId;
    private int rate; // 1..5
    private LocalDateTime timestamp;
}

