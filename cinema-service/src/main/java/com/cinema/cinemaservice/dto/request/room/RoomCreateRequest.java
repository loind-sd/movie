package com.cinema.cinemaservice.dto.request.room;

import lombok.Data;

@Data
public class RoomCreateRequest {
    private String name;
    private Integer totalRows;
    private Integer totalColumns;
}
