package com.example.aws_lambda.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRevenue {
    private String userId;
    private BigDecimal totalRevenue;
}
