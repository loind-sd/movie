package com.cinema.showtimeservice.entity;

import com.cinema.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "show_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Showtime extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;
    private Long movieId;
    private Integer bufferTime; // in minutes
    private String status;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
