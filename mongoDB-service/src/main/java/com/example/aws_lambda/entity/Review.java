package com.example.aws_lambda.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "review")
@Data
public class Review {
    @Id
    private String id;
    private String productId;
    private String userId;
    private Integer rating;
    private String comment;
    private Instant createdAt;
}
