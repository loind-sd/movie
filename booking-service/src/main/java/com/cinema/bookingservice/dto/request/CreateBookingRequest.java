package com.cinema.bookingservice.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateBookingRequest {
    private Long userId;
    private Long showtimeId;
    private BigDecimal totalPrice;

    private List<BookingSeatItem> seats;
}
