package com.cinema.cinemaservice.dto.request.seat;

import com.cinema.common.enums.SeatType;
import lombok.Data;

import java.util.List;

@Data
public class UpdateSeatTypeRequest {
    private Long roomId;
    private SeatType type;

    private Integer fromRow;
    private Integer toRow;
    private Integer fromColumn;
    private Integer toColumn;
}
