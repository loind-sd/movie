package com.cinema.movieservice.specificaton;

import com.cinema.movieservice.dto.request.genre.GenreSearchRequest;
import com.cinema.movieservice.entity.Genre;
import org.springframework.data.jpa.domain.Specification;

public class GenreSpecification {
    public static Specification<Genre> searchGenre(GenreSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + request.getKeyword().toLowerCase() + "%"
                        )
                );
            }

            return predicates;
        };
    }
}
