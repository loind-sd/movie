package com.cinema.debezium.repository;

import com.cinema.debezium.dto.MovieDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieEsRepository extends ElasticsearchRepository<MovieDocument, Long> {
}
