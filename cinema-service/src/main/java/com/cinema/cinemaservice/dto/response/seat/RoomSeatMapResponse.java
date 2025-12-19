package com.cinema.cinemaservice.dto.response.seat;

import com.cinema.cinemaservice.dto.response.room.RoomDetailResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoomSeatMapResponse extends RoomDetailResponse {
    private List<RoomSeatMapItem> seatMap;
}
