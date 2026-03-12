package com.example.aws_lambda.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
@ConfigurationProperties(prefix = "aws")
@Getter
@Setter
public class AwsSesConfig {
    @Bean
    public SesClient sesClient() {
        return SesClient.builder()
                .region(Region.AP_SOUTHEAST_1)
                .build();
    }
}
