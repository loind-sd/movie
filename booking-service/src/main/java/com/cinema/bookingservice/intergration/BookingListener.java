package com.cinema.bookingservice.intergration;

import com.cinema.bookingservice.entity.Booking;
import com.cinema.bookingservice.repository.BookingRepository;
import com.cinema.common.dto.kafkaMessage.BookingCompleteMessage;
import com.cinema.common.enums.BookingStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingListener {
    private final ObjectMapper objectMapper;
    private final BookingRepository bookingRepository;

    @KafkaListener(topics = "booking_complete")
    public void completeBooking(String message) {
        try {
            log.info("[COMPLETE_BOOKING] Received BookingCompleteMessage from topic: {}", message);
            BookingCompleteMessage request = objectMapper.readValue(message, BookingCompleteMessage.class);
            Optional<Booking> bookingOptional = bookingRepository.findById(request.getBookingId());
            if (bookingOptional.isEmpty()) {
                log.error("[COMPLETE_BOOKING] booking {} not found", request.getBookingId());
                return;
            }

            Booking booking = bookingOptional.get();
            if (!Objects.equals(booking.getTotalPrice(), request.getTotalPrice())) {
                log.error("[COMPLETE_BOOKING] booking {}, totalPrice different {} vs {}", request.getBookingId(), booking.getTotalPrice(), request.getTotalPrice());
                return;
            }

            booking.setStatus(BookingStatus.PAID.name());
            bookingRepository.save(booking);
            log.info("[COMPLETE_BOOKING] booking {} process successfully", request.getBookingId());
        } catch (Exception e) {
            log.error("[COMPLETE_BOOKING] error: {}", e.getMessage());
        }
    }

}
