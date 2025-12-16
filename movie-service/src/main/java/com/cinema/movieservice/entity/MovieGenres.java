package com.cinema.movieservice.entity;

import com.cinema.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movie_genres")
@Data
public class MovieGenres extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_id", nullable = false)
    private Long genreId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;
}
