package com.cinema.movieservice.mapper;

import com.cinema.movieservice.dto.response.GenreItemResponse;
import com.cinema.movieservice.entity.Genre;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {
    GenreItemResponse toResponse(Genre entity);

    List<GenreItemResponse> toResponses(Iterable<Genre> entities);

    default Page<GenreItemResponse> toPage(Page<Genre> page) {
        return page.map(this::toResponse);
    }
}

