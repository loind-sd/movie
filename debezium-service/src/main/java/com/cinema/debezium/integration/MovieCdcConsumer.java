package com.cinema.debezium.integration;

import com.cinema.debezium.dto.MovieDocument;
import com.cinema.debezium.repository.MovieEsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieCdcConsumer {
    private final ObjectMapper objectMapper;
    private final MovieEsRepository movieRepository;

    @KafkaListener(topics = "cdc.public.movies")
    public void consume(String message) {
        // 🔥 FIX QUAN TRỌNG
        if (message == null || message.equals("null")) {
            log.debug("Skip tombstone message");
            return;
        }

        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode payload = root.get("payload");

            if (payload.has("__deleted") && payload.get("__deleted").asBoolean()) {
                handleDelete(payload);
            } else {
                handleUpsert(payload);
            }

        } catch (Exception e) {
            log.error("Failed to process CDC message", e);
        }
    }

    private void handleUpsert(JsonNode after) {
        if (after == null) return;

        Long id = after.get("id").asLong();
        String title = after.get("title").asText();

        MovieDocument movieDocument = MovieDocument.builder()
                .id(id)
                .title(title)
                .build();
        movieRepository.save(movieDocument);
        log.info("UPSERT movie id={}, title={}", id, title);
    }

    private void handleDelete(JsonNode before) {
        if (before == null) return;

        Long id = before.get("id").asLong();
        movieRepository.deleteById(id);

        log.info("DELETE movie id={}", id);
    }
}

