package com.example.aws_lambda.entity;

import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
@Getter
@Setter
public class Order {

    private String userId;
    private String orderId;
    private String status;
    private Double totalAmount;
    private Long createdAt;
    private Long ttl;

    @DynamoDbPartitionKey
    public String getUserId() { return userId; }

    @DynamoDbSortKey
    public String getOrderId() { return orderId; }

    @DynamoDbSecondaryPartitionKey(indexNames = "status-createdAt-index")
    public String getStatus() { return status; }

    @DynamoDbSecondarySortKey(indexNames = "status-createdAt-index")
    public Long getCreatedAt() { return createdAt; }

}
