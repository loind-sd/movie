package com.cinema.movieservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.movieservice.dto.request.people.PeopleCreateRequest;
import com.cinema.movieservice.dto.request.people.PeopleUpdateRequest;
import com.cinema.movieservice.service.PeopleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/persons")
public class PersonController {
    private final PeopleService peopleService;

    @PostMapping()
    public ResponseEntity<ServiceResult> create(
            @RequestPart(value = "data") PeopleCreateRequest request,
            @RequestPart(value = "avatarUrl", required = false) MultipartFile avatarUrl
            ) {
        ServiceResult serviceResult = peopleService.create(avatarUrl, request);
        return ResponseEntity.ok(serviceResult);
    }

    @PutMapping()
    public ResponseEntity<ServiceResult> update(
            @RequestPart(value = "data") PeopleUpdateRequest request,
            @RequestPart(value = "avatarUrl", required = false) MultipartFile avatarUrl
            ) {
        ServiceResult serviceResult = peopleService.update(avatarUrl, request);
        return ResponseEntity.ok(serviceResult);
    }
}
