package com.cinema.cinemaservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.cinemaservice.dto.request.room.RoomCreateRequest;
import com.cinema.cinemaservice.dto.request.room.RoomUpdateRequest;
import com.cinema.cinemaservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ServiceResult> create(@RequestBody RoomCreateRequest request) {
        ServiceResult result = roomService.create(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<ServiceResult> update(@RequestBody RoomUpdateRequest request) {
        ServiceResult result = roomService.update(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResult> getDetail(@PathVariable Long id) {
        ServiceResult result = roomService.getDetail(id);
        return ResponseEntity.ok(result);
    }
}
