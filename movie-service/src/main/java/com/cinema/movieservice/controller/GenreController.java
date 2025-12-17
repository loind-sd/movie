package com.cinema.movieservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.movieservice.dto.request.genre.GenreCreateRequest;
import com.cinema.movieservice.dto.request.genre.GenreSearchRequest;
import com.cinema.movieservice.dto.request.genre.GenreUpdateRequest;
import com.cinema.movieservice.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping()
    public ResponseEntity<ServiceResult> getWithPaging(GenreSearchRequest request) {
        ServiceResult result = genreService.getWithPaging(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping()
    public ResponseEntity<ServiceResult> addGenre(@RequestBody GenreCreateRequest request) {
        ServiceResult result = genreService.create(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping()
    public ResponseEntity<ServiceResult> updateGenre(@RequestBody GenreUpdateRequest request) {
        ServiceResult result = genreService.update(request);
        return ResponseEntity.ok(result);
    }
}
