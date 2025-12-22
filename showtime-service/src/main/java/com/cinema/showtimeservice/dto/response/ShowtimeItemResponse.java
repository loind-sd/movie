package com.cinema.showtimeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowtimeItemResponse {
    private Long id;
    private Long movieId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
