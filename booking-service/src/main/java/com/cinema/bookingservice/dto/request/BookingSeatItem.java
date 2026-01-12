package com.cinema.bookingservice.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookingSeatItem {
    private Long seatId;
    private BigDecimal price;
}
