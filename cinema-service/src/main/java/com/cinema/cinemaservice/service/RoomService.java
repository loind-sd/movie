package com.cinema.cinemaservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.cinemaservice.dto.request.room.RoomCreateRequest;
import com.cinema.cinemaservice.dto.request.room.RoomUpdateRequest;

public interface RoomService {
    ServiceResult create(RoomCreateRequest request);
    ServiceResult update(RoomUpdateRequest request);
    ServiceResult getDetail(Long roomId);
    ServiceResult delete(Long roomId);
}
