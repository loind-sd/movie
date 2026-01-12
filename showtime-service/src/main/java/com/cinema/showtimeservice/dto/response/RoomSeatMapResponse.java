package com.cinema.showtimeservice.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RoomSeatMapResponse {
    private Long id;
    private String name;
    private Integer totalRows;
    private Integer totalColumns;
    private List<RoomSeatMapItem> seatMap;
}
