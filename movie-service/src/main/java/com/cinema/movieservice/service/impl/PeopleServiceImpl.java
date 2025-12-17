package com.cinema.movieservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.exception.ErrorCode;
import com.cinema.common.service.MinioService;
import com.cinema.movieservice.dto.request.people.PeopleCreateRequest;
import com.cinema.movieservice.dto.request.people.PeopleUpdateRequest;
import com.cinema.movieservice.entity.Person;
import com.cinema.movieservice.repository.PersonRepository;
import com.cinema.movieservice.service.PeopleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PeopleServiceImpl implements PeopleService {
    private final PersonRepository personRepository;
    private final MinioService minioService;

    @Override
    public ServiceResult create(MultipartFile avatarUrl, PeopleCreateRequest request) {
        log.debug("Create People");
        Person person = new Person();
        person.setName(request.getName());
        person.setBirthDate(request.getBirthDate());
        person.setRole(request.getRole());

        if (avatarUrl != null) {
            String objectPath = minioService.buildAvatarPath(avatarUrl.getOriginalFilename());
            try {
                String avatarUrlStr = minioService.upload(objectPath, avatarUrl.getInputStream(), avatarUrl.getSize(), avatarUrl.getContentType());
                person.setAvatarUrl(avatarUrlStr);
            } catch (Exception e) {
                log.error("Upload avatar failed", e);
            }
        }

        personRepository.save(person);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult update(MultipartFile avatarUrl, PeopleUpdateRequest request) {
        log.debug("Update People");
        Optional<Person> personOpt = personRepository.findById(request.getId());
        if (personOpt.isEmpty()) {
            return ServiceResult.fail(ErrorCode.PERSON_NOT_FOUND);
        }

        Person person = personOpt.get();
        person.setName(request.getName());
        person.setBirthDate(request.getBirthDate());
        person.setRole(request.getRole());

        if (avatarUrl != null) {
            String objectPath = minioService.buildAvatarPath(avatarUrl.getOriginalFilename());
            try {
                String avatarUrlStr = minioService.upload(objectPath, avatarUrl.getInputStream(), avatarUrl.getSize(), avatarUrl.getContentType());
                person.setAvatarUrl(avatarUrlStr);
            } catch (Exception e) {
                log.error("Upload avatar failed", e);
            }
        }
        personRepository.save(person);
        return ServiceResult.ok();
    }
}
