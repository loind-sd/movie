package com.cinema.movieservice.service.searchES;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.cinema.common.dto.PageResult;
import com.cinema.movieservice.dto.es_model.MovieDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public PageResult<MovieDocument> search(
            String keyword,
            List<String> genres,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) throws IOException {

        SearchResponse<MovieDocument> response =
                elasticsearchClient.search(s -> s
                                .index("movies")
                                .from(pageable.getPageNumber() * pageable.getPageSize())
                                .size(pageable.getPageSize())
                                .query(q -> q
                                        .bool(b -> {
                                            if (keyword != null && !keyword.isBlank()) {
                                                b.must(m -> m
                                                        .multiMatch(mm -> mm
                                                                .query(keyword)
                                                                .fields("title", "title.ascii", "actors")
                                                                .fuzziness("AUTO")
                                                        )
                                                );
                                            }

                                            if (genres != null && !genres.isEmpty()) {
                                                b.filter(f -> f
                                                        .terms(t -> t
                                                                .field("genres")
                                                                .terms(v -> v.value(
                                                                        genres.stream()
                                                                                .map(FieldValue::of)
                                                                                .toList()
                                                                ))
                                                        )
                                                );
                                            }

                                            if (fromDate != null || toDate != null) {
                                                b.filter(f -> f
                                                        .range(r -> r
                                                                .field("releaseDate")
                                                                .gte(fromDate != null ? JsonData.fromJson(fromDate.toString()) : null)
                                                                .lte(toDate != null ? JsonData.fromJson(toDate.toString()) : null)
                                                        )
                                                );
                                            }

                                            return b;
                                        })
                                ),
                        MovieDocument.class
                );

        long total = response.hits().total().value();

        return new PageResult<>(
                response.hits().hits()
                        .stream()
                        .map(Hit::source)
                        .toList(),
                total,
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    public List<String> suggest(String keyword) throws IOException {

        SearchResponse<MovieDocument> response =
                elasticsearchClient.search(s -> s
                                .index("movies")
                                .size(5)
                                .query(q -> q
                                        .match(m -> m
                                                .field("title.ascii")
                                                .query(keyword)
                                        )
                                ),
                        MovieDocument.class
                );

        return response.hits().hits()
                .stream()
                .map(hit -> {
                    assert hit.source() != null;
                    return hit.source().getTitle();
                })
                .distinct()
                .toList();
    }


}

