package com.cinema.common.dto.kafkaMessage;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MovieViewEvent {
    private String userId;
    private String movieId;
    private LocalDateTime timestamp;
}
