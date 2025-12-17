package com.cinema.movieservice.dto.request.people;

import com.cinema.common.enums.PersonRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PeopleCreateRequest {
    private String name;
    private LocalDate birthDate;
    private PersonRole role;
}
