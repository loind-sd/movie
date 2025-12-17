package com.cinema.movieservice.dto.request.genre;

import com.cinema.common.dto.CommonPageable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenreSearchRequest extends CommonPageable {
    private String keyword;
}
