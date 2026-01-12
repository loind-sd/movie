package com.cinema.bookingservice.controller;

import com.cinema.bookingservice.dto.request.CreateBookingRequest;
import com.cinema.bookingservice.service.BookingService;
import com.cinema.common.base.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping()
    public ResponseEntity<ServiceResult> book(@RequestBody CreateBookingRequest request) {
        ServiceResult result = bookingService.book(request);
        return ResponseEntity.ok(result);
    }
}
