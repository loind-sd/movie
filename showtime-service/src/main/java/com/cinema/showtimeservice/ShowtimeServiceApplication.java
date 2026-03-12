package com.cinema.showtimeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.cinema.common", "com.cinema.showtimeservice", "com.cinema.kafka", "com.cinema.security"})
public class ShowtimeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShowtimeServiceApplication.class, args);
    }
}
