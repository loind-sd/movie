package com.cinema.movieservice.entity;

import com.cinema.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "movie_people")
@Data
public class MoviePeople extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "people_id", nullable = false)
    private Long peopleId;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @Column(name = "role", nullable = false)
    private String role; // e.g., "ACTOR", "DIRECTOR"
}
