package com.cinema.showtimeservice.dto.response;

import com.cinema.common.enums.BookingStatus;
import com.cinema.common.enums.SeatStatus;
import com.cinema.common.enums.SeatType;
import lombok.Data;

@Data
public class RoomSeatMapItem {
    private Integer rowIndex;
    private Integer columnIndex;
    private SeatType type;
    private SeatStatus status;
    private BookingStatus bookingStatus = BookingStatus.EMPTY;
    private String label;
}
