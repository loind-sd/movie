package com.cinema.showtimeservice.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SearchShowtimeRequest {
    private Long movieId;
    private LocalDate date;
}
