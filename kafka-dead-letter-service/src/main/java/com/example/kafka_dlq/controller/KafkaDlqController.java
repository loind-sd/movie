package com.example.kafka_dlq.controller;

import com.cinema.kafka.procedure.KafkaProducer;
import com.example.kafka_dlq.dto.KafkaMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dlq")
@RequiredArgsConstructor
public class KafkaDlqController {

    private final KafkaProducer kafkaProducer;

    @PostMapping("/send")
    public String send(@RequestBody KafkaMessage message) {
        kafkaProducer.send("orders", message);
        return "sent";
    }
}
