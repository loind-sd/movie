package com.cinema.paymentservice.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCreateRequest {
    private Long bookingId;
    private BigDecimal totalPrice;
}
