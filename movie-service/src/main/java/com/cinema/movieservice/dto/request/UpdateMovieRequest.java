package com.cinema.movieservice.dto.request;

import com.cinema.common.enums.AgeRating;

import java.time.LocalDate;
import java.util.Set;

public record UpdateMovieRequest(
        Long id,
        String title,
        String originalTitle,
        String description,
        Integer duration,
        LocalDate releaseDate,
        AgeRating ageRating,
        Set<Long> genreIds,
        Set<Long> castIds,
        Set<Long> directorIds,
        String trailerUrl
) {}
