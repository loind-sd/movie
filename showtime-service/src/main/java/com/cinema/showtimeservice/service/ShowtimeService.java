package com.cinema.showtimeservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.showtimeservice.dto.request.CreateShowtimeRequest;
import com.cinema.showtimeservice.dto.request.SearchShowtimeRequest;
import com.cinema.showtimeservice.dto.request.UpdateShowtimeRequest;

public interface ShowtimeService {
    ServiceResult create(CreateShowtimeRequest request);
    ServiceResult getDetails(Long showtimeId);
    ServiceResult update(UpdateShowtimeRequest request);
    ServiceResult searchShowTime(SearchShowtimeRequest request);
}
