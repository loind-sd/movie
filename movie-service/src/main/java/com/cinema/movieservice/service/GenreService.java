package com.cinema.movieservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.movieservice.dto.request.genre.GenreCreateRequest;
import com.cinema.movieservice.dto.request.genre.GenreSearchRequest;
import com.cinema.movieservice.dto.request.genre.GenreUpdateRequest;

public interface GenreService {
    ServiceResult create(GenreCreateRequest request);
    ServiceResult update(GenreUpdateRequest request);
    ServiceResult delete(Long genreId);
    ServiceResult getWithPaging(GenreSearchRequest request);
}
