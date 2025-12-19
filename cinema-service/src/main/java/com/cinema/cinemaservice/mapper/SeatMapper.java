package com.cinema.cinemaservice.mapper;

import com.cinema.cinemaservice.dto.response.seat.RoomSeatMapItem;
import com.cinema.cinemaservice.entity.Seat;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    RoomSeatMapItem toRoomSeatMapItem(Seat seat);
    List<RoomSeatMapItem> toListRoomSeatMapItem(Iterable<Seat> seats);
}