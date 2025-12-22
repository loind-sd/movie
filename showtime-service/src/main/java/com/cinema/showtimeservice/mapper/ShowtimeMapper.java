package com.cinema.showtimeservice.mapper;

import com.cinema.showtimeservice.dto.response.ShowtimeDetailResponse;
import com.cinema.showtimeservice.entity.Showtime;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShowtimeMapper {
    ShowtimeDetailResponse toShowtimeDetailResponse(Showtime showtime);
}
