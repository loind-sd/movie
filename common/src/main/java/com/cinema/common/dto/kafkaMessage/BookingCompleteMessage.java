package com.cinema.common.dto.kafkaMessage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCompleteMessage {
    private Long bookingId;
    private BigDecimal totalPrice;
}
