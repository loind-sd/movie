package com.cinema.cinemaservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.cinemaservice.dto.request.seat.UpdateSeatTypeRequest;
import com.cinema.cinemaservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @PutMapping
    public ResponseEntity<ServiceResult> updateSeatStatus(@RequestBody UpdateSeatTypeRequest request) {
        ServiceResult result = seatService.updateSeatStatus(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/seatMap/{roomId}")
    public ResponseEntity<ServiceResult> getSeatMapByRoomId(@PathVariable Long roomId) {
        ServiceResult result = seatService.getRoomSeats(roomId);
        return ResponseEntity.ok(result);
    }
}
