package com.cinema.movieservice.dto.response;

import com.cinema.common.constants.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDetailResponse {
    private Long id;
    private String title;
    private String originalTitle;
    private String description;
    private Integer duration;
    @JsonFormat(pattern = DateFormatConstants.C_DD_MM_YYYY)
    private LocalDate releaseDate;
    private String ageRating;

    private String posterUrl;
    private String thumbnailUrl;
    private String trailerUrl;

    private List<PeopleResponse> directors;
    private List<PeopleResponse> actors;
    private List<GenreItemResponse> genres;
}
