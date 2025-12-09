package com.cinema.common.dto;

import com.cinema.common.enums.MovieStatus;

public class MovieDto {
    private Long id;
    private String title;
    private Integer durationMinutes;
    private MovieStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public MovieStatus getStatus() { return status; }
    public void setStatus(MovieStatus status) { this.status = status; }
}
