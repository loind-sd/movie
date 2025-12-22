package com.cinema.showtimeservice.repository;

import com.cinema.showtimeservice.dto.request.SearchShowtimeRequest;
import com.cinema.showtimeservice.dto.response.ShowtimeItemResponse;

import java.util.List;

public interface ShowtimeRepositoryCustom {
    List<ShowtimeItemResponse> searchShowTime(SearchShowtimeRequest request);
}
