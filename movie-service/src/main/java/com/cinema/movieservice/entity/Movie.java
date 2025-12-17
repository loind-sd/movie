package com.cinema.movieservice.entity;

import com.cinema.common.entity.BaseEntity;
import com.cinema.common.enums.AgeRating;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movies")
@Data
public class Movie extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String originalTitle;

    @Column(length = 2000)
    private String description;

    private Integer duration; // phút
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    private AgeRating ageRating;

    private String posterUrl;
    private String thumbnailUrl;
    private String trailerUrl;

    private Boolean active;
}

