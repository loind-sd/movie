package com.cinema.showtimeservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.showtimeservice.dto.request.CreateShowtimeRequest;
import com.cinema.showtimeservice.dto.request.SearchShowtimeRequest;
import com.cinema.showtimeservice.dto.request.UpdateShowtimeRequest;
import com.cinema.showtimeservice.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/showtime")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<ServiceResult> create(@RequestBody CreateShowtimeRequest request) {
        ServiceResult result = showtimeService.create(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResult> getDetails(@PathVariable Long id) {
        ServiceResult result = showtimeService.getDetails(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping()
    public ResponseEntity<ServiceResult> searchShowTime(SearchShowtimeRequest request) {
        ServiceResult result = showtimeService.searchShowTime(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/seatMap/{id}")
    public ResponseEntity<ServiceResult> searchShowTime(@PathVariable Long id) {
        ServiceResult result = showtimeService.getShowtimeMap(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping()
    public ResponseEntity<ServiceResult> update(@RequestBody UpdateShowtimeRequest request) {
        ServiceResult result = showtimeService.update(request);
        return ResponseEntity.ok(result);
    }
}
