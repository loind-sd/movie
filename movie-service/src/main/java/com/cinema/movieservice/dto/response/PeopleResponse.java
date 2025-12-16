package com.cinema.movieservice.dto.response;

import com.cinema.common.constants.DateFormatConstants;
import com.cinema.common.enums.PersonRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeopleResponse {
    private Long id;
    private String name;

    @JsonFormat(pattern = DateFormatConstants.C_DD_MM_YYYY)
    private LocalDate birthDate;

    private PersonRole role;
    private String avatarUrl;
}
