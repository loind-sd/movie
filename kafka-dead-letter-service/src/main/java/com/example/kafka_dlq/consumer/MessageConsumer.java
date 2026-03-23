package com.example.kafka_dlq.consumer;

import com.example.kafka_dlq.dto.KafkaMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageConsumer {

    private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 5000)
    )
    @KafkaListener(topics = "orders", groupId = "order-group")
    public void consume(String message) {

        log.info("Processing order: {}", message);

        KafkaMessage kafkaMessage;
        try {
            kafkaMessage = objectMapper.readValue(message, KafkaMessage.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if (kafkaMessage.getQuantity() > 3) {
            throw new RuntimeException("Invalid quantity");
        }

        log.info("Order processed");
    }

    @KafkaListener(topics = "orders-dlt", groupId = "order-group")
    public void processDLT(String order) {
        log.info("DLT received: {}", order);

        if (meterRegistry != null) {
            Counter.builder("kafka_dlt_total")
                    .description("Total Kafka messages sent to DLT")
                    .tag("topic", "orders-dlt")
                    .register(meterRegistry)
                    .increment();
        }
    }
}
