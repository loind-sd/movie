package com.example.aws_lambda.controller;

import com.example.aws_lambda.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {
    @GetMapping
    public String ok() {
        return "ok";
    }

    private final S3Service s3Service;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        return s3Service.upload(file);
    }

    @GetMapping("/{key}")
    public String getPresign(@PathVariable String key) {
        return s3Service.generatePresignedDownloadUrl(key);
    }
}
