package com.cinema.cinemaservice.dto.request.room;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoomUpdateRequest extends RoomCreateRequest {
    private Long id;
}
