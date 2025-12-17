package com.cinema.movieservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.movieservice.dto.request.movie.CreateMovieRequest;
import com.cinema.movieservice.dto.request.movie.UpdateMovieRequest;
import com.cinema.movieservice.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createMovies(
            @RequestPart(value = "poster", required = false) MultipartFile poster,
            @Valid @RequestPart("data") CreateMovieRequest request
    ) {
        ServiceResult result = movieService.create(poster, request);
        return ResponseEntity.ok(result);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> update(
            @RequestPart(value = "poster", required = false) MultipartFile poster,
            @Valid @RequestPart("data") UpdateMovieRequest request
    ) {
        ServiceResult result = movieService.update(poster, request);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> delete(@PathVariable Long movieId) {
        ServiceResult result = movieService.delete(movieId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> getDetail(
            @PathVariable("movieId") Long movieId
    ) {
        ServiceResult result = movieService.getDetail(movieId);
        return ResponseEntity.ok(result);
    }



}
