package com.cinema.movieservice.service;

import com.cinema.movieservice.config.MinIOConfigProperties;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final MinIOConfigProperties properties;

    /* ================= UPLOAD ================= */

    public String upload(
            String objectPath,
            MultipartFile file
    ) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectPath)
                            .contentType(file.getContentType())
                            .stream(
                                    file.getInputStream(),
                                    file.getSize(),
                                    -1
                            )
                            .build()
            );
            return buildPublicUrl(objectPath);
        } catch (Exception e) {
            log.error("Upload failed: {}", objectPath, e);
            throw new RuntimeException("Upload file failed");
        }
    }

    public String upload(
            String objectPath,
            InputStream inputStream,
            long size,
            String contentType
    ) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectPath)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build()
            );
            return buildPublicUrl(objectPath);
        } catch (Exception e) {
            log.error("Upload stream failed: {}", objectPath, e);
            throw new RuntimeException("Upload stream failed");
        }
    }

    /* ================= DOWNLOAD ================= */

    public InputStream download(String objectPath) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectPath)
                            .build()
            );
        } catch (Exception e) {
            log.error("Download failed: {}", objectPath, e);
            throw new RuntimeException("Download file failed");
        }
    }

    public byte[] downloadAsBytes(String objectPath) {
        try (InputStream is = download(objectPath)) {
            return is.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Read file failed");
        }
    }

    /* ================= URL ================= */

    /** Public URL (nếu bucket public) */
    public String buildPublicUrl(String objectPath) {
        return properties.getPublicUrl()
                + "/" + properties.getBucket()
                + "/" + objectPath;
    }

    /** Presigned URL (an toàn, có thời hạn) */
    public String generatePresignedUrl(
            String objectPath,
            int expirySeconds
    ) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(properties.getBucket())
                            .object(objectPath)
                            .expiry(expirySeconds)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Generate presigned URL failed");
        }
    }

    /* ================= DELETE ================= */

    public void delete(String objectPath) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectPath)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Delete object failed");
        }
    }

    /* ================= EXISTS ================= */

    public boolean exists(String objectPath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(properties.getBucket())
                            .object(objectPath)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            if ("NoSuchKey".equals(e.errorResponse().code())) {
                return false;
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* ================= UTILS ================= */

    public String buildMoviePosterPath(Long movieId) {
        return "posters/movie/" + movieId + ".jpg";
    }

    public String buildMovieThumbnailPath(Long movieId) {
        return "thumbnails/movie/" + movieId + ".jpg";
    }

    public String buildMovieTrailerPath(Long movieId) {
        return "trailers/movie/" + movieId + ".mp4";
    }
}
