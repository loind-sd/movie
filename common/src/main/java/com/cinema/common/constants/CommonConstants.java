package com.cinema.common.constants;

public interface CommonConstants {
    interface RedisKey {
        String SEAT_KEY_PREFIX = "SHOWTIME_";
        String SEAT_KEY = SEAT_KEY_PREFIX + "%d_SEAT_%d";
        String BOOKING_KEY = "BOOKING_USER_ID_%d_SHOWTIME_ID_%d";
        String TOKEN_KEY = "TOKEN_%s";
    }

    interface KafkaTopic {
        String BOOKING_COMPLETE = "booking_complete";
    }
}
