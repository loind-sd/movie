package com.cinema.common.auto_config;

import com.cinema.common.config.MinIOConfigProperties;
import com.cinema.common.service.MinioService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(MinIOConfigProperties.class)
@ConditionalOnClass(MinioClient.class)
@ConditionalOnProperty(prefix = "minio", name = "url")
public class MinioAutoConfiguration {

    /**
     * Tạo bean MinioClient nếu chưa có bean nào cùng loại được định nghĩa trong ngữ cảnh ứng dụng.
     * Sử dụng cấu hình từ MinIOConfigProperties để khởi tạo MinioClient.
     */
    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(MinIOConfigProperties props) {
        return MinioClient.builder()
                .endpoint(props.getUrl())
                .credentials(props.getAccessKey(), props.getSecretKey())
                .build();
    }

    /**
     * Tạo bean MinioService nếu chưa có bean nào cùng loại được định nghĩa trong ngữ cảnh ứng dụng.
     * Sử dụng MinioClient và MinIOConfigProperties để khởi tạo MinioService.
     */
    @Bean
    @ConditionalOnMissingBean
    public MinioService minioService(
            MinioClient minioClient,
            MinIOConfigProperties properties
    ) {
        return new MinioService(minioClient, properties);
    }

    /**
     * Tạo ApplicationRunner để khởi tạo bucket trong MinIO nếu chưa tồn tại.
     * Sử dụng MinioClient và MinIOConfigProperties để kiểm tra và tạo bucket.
     */
    @Bean
    @ConditionalOnProperty(prefix = "minio", name = "bucket")
    public ApplicationRunner initBucket(
            MinioClient minioClient,
            MinIOConfigProperties props
    ) {
        return args -> {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(props.getBucket())
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(props.getBucket())
                                .build()
                );
            }
        };
    }
}

