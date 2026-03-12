package com.example.aws_lambda.repository;

import com.example.aws_lambda.dto.UserRevenue;
import com.example.aws_lambda.entity.Order;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Order> findByCreatedAtBetween(Instant from, Instant to);

    @Aggregation(pipeline = {
            "{ $group: { _id: '$userId', totalRevenue: { $sum: '$totalAmount' } } }"
    })
    List<UserRevenue> calculateRevenue();

    @Aggregation(pipeline = {
            "{ $match: { createdAt: { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$userId', totalRevenue: { $sum: '$totalAmount' } } }"
    })
    List<UserRevenue> calculateRevenue(Instant from, Instant to);
}
