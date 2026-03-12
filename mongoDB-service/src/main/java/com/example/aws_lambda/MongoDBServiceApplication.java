package com.example.aws_lambda;

import com.example.aws_lambda.entity.Order;
import com.example.aws_lambda.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@SpringBootApplication
public class MongoDBServiceApplication {

    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    public void init() {
        Order.OrderItem orderItem = new Order.OrderItem();
        orderItem.setProductId("69a6818fb0575f26b8937c82");
        orderItem.setPrice(BigDecimal.valueOf(20000));
        orderItem.setQuantity(1);
        orderItem.setProductName("Product 1");

        Order order = new Order();
        order.setCreatedAt(Instant.now());
        order.setUserId("69a680eacebbcb13eda667b5");
        order.setTotalAmount(BigDecimal.valueOf(20000));
        order.setItems(List.of(orderItem));

        orderRepository.save(order);
    }

    public static void main(String[] args) {
        SpringApplication.run(MongoDBServiceApplication.class, args);
    }
}
