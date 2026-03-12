package com.cinema.common.dto.kafkaDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingAgg {
    private long total;
    private long count;

    public void add(int rate) {
        this.total += rate;
        this.count++;
    }

    public double avg() {
        return count == 0 ? 0.0 : (double) total / count;
    }
}

