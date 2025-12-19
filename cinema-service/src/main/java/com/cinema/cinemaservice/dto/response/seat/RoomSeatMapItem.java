package com.cinema.cinemaservice.dto.response.seat;

import com.cinema.common.enums.SeatStatus;
import com.cinema.common.enums.SeatType;
import lombok.Data;

@Data
public class RoomSeatMapItem {
    private Integer rowIndex;
    private Integer columnIndex;
    private SeatType type;
    private SeatStatus status;
    private String label;
}
