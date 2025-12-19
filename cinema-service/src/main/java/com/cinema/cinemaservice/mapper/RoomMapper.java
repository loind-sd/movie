package com.cinema.cinemaservice.mapper;

import com.cinema.cinemaservice.dto.response.room.RoomDetailResponse;
import com.cinema.cinemaservice.entity.Room;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RoomMapper {
    RoomDetailResponse toRoomDetailResponse(Room room);
}