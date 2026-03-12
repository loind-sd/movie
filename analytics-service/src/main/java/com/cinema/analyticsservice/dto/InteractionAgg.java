package com.cinema.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InteractionAgg {
    private int score;
    private long lastUpdated;

    public void add(int value) {
        this.score += value;
        this.lastUpdated = System.currentTimeMillis();
    }
}

