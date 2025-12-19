package com.cinema.cinemaservice.entity;

import com.cinema.common.entity.BaseEntity;
import com.cinema.common.enums.SeatStatus;
import com.cinema.common.enums.SeatType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "seats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "row_index", "column_index"}))
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    private Integer rowIndex;     // 1,2,3...
    private Integer columnIndex;  // 1,2,3...

    @Enumerated(EnumType.STRING)
    private SeatType type;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    private String label; // A1, B2, C10
}

