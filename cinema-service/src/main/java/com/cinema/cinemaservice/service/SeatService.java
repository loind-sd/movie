package com.cinema.cinemaservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.cinemaservice.dto.request.seat.UpdateSeatTypeRequest;

public interface SeatService {
    ServiceResult updateSeatStatus(UpdateSeatTypeRequest request);
    ServiceResult getRoomSeats(Long roomId);
}
