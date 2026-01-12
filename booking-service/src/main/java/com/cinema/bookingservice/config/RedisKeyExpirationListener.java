package com.cinema.bookingservice.config;

import com.cinema.bookingservice.service.BookingService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final BookingService bookingService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer, BookingService bookingService) {
        super(listenerContainer);
        this.bookingService = bookingService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();

        if (key.startsWith("BOOKING_USER_ID_")) {
            bookingService.handleBookingExpired(key);
        }
    }
}

