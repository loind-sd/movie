package com.cinema.movieservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@ComponentScan(basePackages = {"com.cinema.movieservice", "com.cinema.common"})
@EnableAsync
public class MovieServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieServiceApplication.class, args);
    }
}