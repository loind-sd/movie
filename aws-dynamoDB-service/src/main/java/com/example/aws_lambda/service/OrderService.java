package com.example.aws_lambda.service;

import com.example.aws_lambda.entity.Order;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final DynamoDbTable<Order> table;

    public OrderService(DynamoDbEnhancedClient client) {
        this.table = client.table("orders",
                TableSchema.fromBean(Order.class));
    }

    public void createOrder(String userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderId(UUID.randomUUID().toString());
        order.setStatus("PENDING");
        order.setTotalAmount(10000.0);
        order.setCreatedAt(System.currentTimeMillis());
        order.setTtl(Instant.now().plus(7, ChronoUnit.DAYS).getEpochSecond());

        table.putItem(order);
    }

    public List<Order> findByUser(String userId) {
        return table.query(r -> r.queryConditional(
                QueryConditional.keyEqualTo(
                        Key.builder().partitionValue(userId).build()
                ))).items().stream().toList();
    }
}
