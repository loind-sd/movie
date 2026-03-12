package com.cinema.common.dto.kafkaDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieRatingAllTime {
    private String movieId;
    private double avgRating;
    private long totalRatings;
}

