package com.cinema.movieservice.repository;

import com.cinema.movieservice.dto.response.GenreItemResponse;
import com.cinema.movieservice.entity.MovieGenres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenresRepository extends JpaRepository<MovieGenres, Long> {
    @Query(value = "select new com.cinema.movieservice.dto.response.GenreItemResponse(g.id, g.name) " +
            "from MovieGenres mg " +
            "join Genre g on mg.genreId = g.id " +
            "where mg.movieId = :id")
    List<GenreItemResponse> findAllByMovieId(Long id);
}
