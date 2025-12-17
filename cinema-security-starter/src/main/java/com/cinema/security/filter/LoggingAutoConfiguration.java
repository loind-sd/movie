package com.cinema.security.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingAutoConfiguration {

    @Bean
    public SimpleRequestLoggingFilter simpleRequestLoggingFilter() {
        return new SimpleRequestLoggingFilter();
    }
}

