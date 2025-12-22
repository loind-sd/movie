package com.cinema.showtimeservice.dto.request;

import lombok.Data;

@Data
public class ShowtimePriceRequest {
    private String seatType;
    private Double price;
}
