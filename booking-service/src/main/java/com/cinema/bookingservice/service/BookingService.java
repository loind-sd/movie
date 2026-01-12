package com.cinema.bookingservice.service;

import com.cinema.bookingservice.dto.request.CreateBookingRequest;
import com.cinema.common.base.ServiceResult;

public interface BookingService {
    ServiceResult book(CreateBookingRequest request);

    ServiceResult handleBookingExpired(String redisKey);
}
