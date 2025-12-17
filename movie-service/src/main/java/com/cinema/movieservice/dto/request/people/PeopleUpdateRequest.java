package com.cinema.movieservice.dto.request.people;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PeopleUpdateRequest extends PeopleCreateRequest {
    private Long id;
}
