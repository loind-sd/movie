package com.cinema.movieservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.exception.ErrorCode;
import com.cinema.movieservice.dto.request.genre.GenreCreateRequest;
import com.cinema.movieservice.dto.request.genre.GenreSearchRequest;
import com.cinema.movieservice.dto.request.genre.GenreUpdateRequest;
import com.cinema.movieservice.entity.Genre;
import com.cinema.movieservice.mapper.GenreMapper;
import com.cinema.movieservice.repository.GenreRepository;
import com.cinema.movieservice.service.GenreService;
import com.cinema.movieservice.specificaton.GenreSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public ServiceResult create(GenreCreateRequest request) {
        log.debug("create genre request: {}", request);
        Optional<Genre> genreOptional = genreRepository.findByCode(request.code());
        if (genreOptional.isPresent()) {
            log.debug("Genre with code {} already exists", request.code());
            return ServiceResult.fail(ErrorCode.GENRE_ALREADY_EXISTS);
        }

        Genre genre = Genre.builder()
                .name(request.name())
                .code(request.code())
                .build();
        genreRepository.save(genre);
        log.debug("Genre created successfully: {}", genre);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult update(GenreUpdateRequest request) {
        log.debug("update genre request: {}", request);
        Optional<Genre> genreOptional = genreRepository.findById(request.id());
        if (genreOptional.isEmpty()) {
            log.debug("Genre with id {} not found", request.id());
            return ServiceResult.fail(ErrorCode.GENRE_NOT_FOUND);
        }
        Genre genre = genreOptional.get();
        genre.setName(request.name());
        genreRepository.save(genre);
        log.debug("Genre updated successfully: {}", genre);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult delete(Long genreId) {
        log.debug("delete genre with id {}", genreId);
        Optional<Genre> genreOptional = genreRepository.findById(genreId);
        if (genreOptional.isEmpty()) {
            log.debug("Genre with id {} not found", genreId);
            return ServiceResult.fail(ErrorCode.GENRE_NOT_FOUND);
        }
        genreRepository.deleteById(genreId);
        log.debug("Genre with id {} deleted successfully", genreId);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult getWithPaging(GenreSearchRequest request) {
        Page<Genre> page = genreRepository.findAll(
                GenreSpecification.searchGenre(request), request.getPageable()
        );
        return ServiceResult.ok(genreMapper.toPage(page));
    }
}
