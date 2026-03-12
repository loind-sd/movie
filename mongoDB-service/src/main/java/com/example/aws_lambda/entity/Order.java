package com.example.aws_lambda.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "orders")
@Data
@CompoundIndex(
        name = "user_created_idx",
        def = "{'userId':1,'createdAt':-1}"
)
public class Order {

    @Id
    private String id;

    private String userId;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal totalAmount;

    @Indexed
    private Instant createdAt;

    private List<OrderItem> items;

    @Data
    public static class OrderItem {

        private String productId;

        private String productName;

        private BigDecimal price;

        private int quantity;
    }
}
