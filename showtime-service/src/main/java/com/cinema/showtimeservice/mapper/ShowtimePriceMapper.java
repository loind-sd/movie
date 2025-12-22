package com.cinema.showtimeservice.mapper;

import com.cinema.showtimeservice.dto.request.ShowtimePriceRequest;
import com.cinema.showtimeservice.entity.ShowtimePrice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ShowtimePriceMapper {
    List<ShowtimePriceRequest> toShowtimePriceItems(List<ShowtimePrice> prices);
}
