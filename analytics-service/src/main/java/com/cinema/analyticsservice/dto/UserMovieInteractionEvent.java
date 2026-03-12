package com.cinema.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMovieInteractionEvent {
    private String userId;
    private String movieId;
    private int score;
}

