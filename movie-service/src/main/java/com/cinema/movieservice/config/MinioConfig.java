package com.cinema.movieservice.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MinIOConfigProperties.class)
public class MinioConfig {

    private final MinIOConfigProperties minIOConfigProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minIOConfigProperties.getUrl())
                .credentials(
                        minIOConfigProperties.getAccessKey(),
                        minIOConfigProperties.getSecretKey()
                )
                .build();
    }

    @Bean
    public ApplicationRunner initBucket(MinioClient minioClient) {
        return args -> {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(minIOConfigProperties.getBucket())
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(minIOConfigProperties.getBucket())
                                .build()
                );
            }
        };
    }
}

