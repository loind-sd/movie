package com.cinema.analyticsservice.integration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalyticsListener {

    @KafkaListener(topics = "analytics.movie.views")
    public void consume(String message) {
        // m1|views=123
        String[] parts = message.split("\\|");
        String movieId = parts[0];
        log.info("analytics.movie.views: {}", movieId);
    }

    @KafkaListener(topics = "analytics.movie.rating.alltime")
    public void consumeRatingAllTime(String message) {
        String[] parts = message.split("\\|");
        String movieId = parts[0];
        log.info("analytics.movie.rating.alltime: {}", movieId);
    }

    @KafkaListener(topics = "analytics.user.movie.interaction")
    public void consumeUserMovieInteraction(String message) {
        String[] parts = message.split("\\|");
        String movieId = parts[0];
        log.info("analytics.user.movie.interaction: {}", movieId);
    }
}
