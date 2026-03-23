package com.example.kafka_dlq;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.example.kafka_dlq",
                "com.cinema.kafka"
        })
public class KafkaDlqApplication {
     public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(KafkaDlqApplication.class, args);
    }
}
