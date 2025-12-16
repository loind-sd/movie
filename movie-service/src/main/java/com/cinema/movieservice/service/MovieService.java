package com.cinema.movieservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.movieservice.dto.request.CreateMovieRequest;
import com.cinema.movieservice.dto.request.UpdateMovieRequest;
import org.springframework.web.multipart.MultipartFile;

public interface MovieService {
    ServiceResult create(MultipartFile poster, CreateMovieRequest request);
    ServiceResult update(MultipartFile poster, UpdateMovieRequest request);
    ServiceResult delete(Long movieId);
    ServiceResult getDetail(Long movieId);
}
