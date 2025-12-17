package com.cinema.common.dto;

import io.netty.util.internal.StringUtil;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Data
public class CommonPageable {
    private Integer page;
    private Integer pageSize;
    private String sortBy;
    private String sortDir;

    public Pageable getPageable() {
        int pageIndex = (page != null && page > 0) ? page - 1 : 0;
        int size = (pageSize != null && pageSize > 0) ? pageSize : 20;

        if (!StringUtil.isNullOrEmpty(sortBy)) {
            Sort.Direction direction = Objects.equals(sortDir, "desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            return PageRequest.of(pageIndex, size, Sort.by(direction, sortBy));
        } else {
            return PageRequest.of(pageIndex, size);
        }
    }
}
