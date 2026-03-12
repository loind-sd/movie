package com.cinema.common.dto.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieViewAnalytics {
    private String movieId;
    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;
    private Long views;
}
