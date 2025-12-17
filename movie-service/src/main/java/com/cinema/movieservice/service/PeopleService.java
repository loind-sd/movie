package com.cinema.movieservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.movieservice.dto.request.people.PeopleCreateRequest;
import com.cinema.movieservice.dto.request.people.PeopleUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

public interface PeopleService {
    ServiceResult create(MultipartFile avatarUrl, PeopleCreateRequest request);
    ServiceResult update(MultipartFile avatarUrl, PeopleUpdateRequest request);
}
