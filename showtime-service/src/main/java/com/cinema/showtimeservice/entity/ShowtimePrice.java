package com.cinema.showtimeservice.entity;

import com.cinema.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "show_time_price")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimePrice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long showtimeId;
    private String seatType;
    private Double price;
}
