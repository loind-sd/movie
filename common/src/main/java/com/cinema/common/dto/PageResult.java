package com.cinema.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class PageResult<T> {
    private List<T> content;
    private long total;
    private int page;
    private int size;
}
