package com.cinema.showtimeservice.repository.impl;

import com.cinema.common.utils.SqlUtil;
import com.cinema.showtimeservice.dto.request.SearchShowtimeRequest;
import com.cinema.showtimeservice.dto.response.ShowtimeItemResponse;
import com.cinema.showtimeservice.repository.ShowtimeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class ShowtimeRepositoryImpl implements ShowtimeRepositoryCustom {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<ShowtimeItemResponse> searchShowTime(SearchShowtimeRequest request) {
        List<ShowtimeItemResponse> responses = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();

        StringBuilder sqlBuilder = new StringBuilder(" from show_time s ");
        sqlBuilder.append(" WHERE 1 = 1 ");

        if (request.getMovieId() != null) {
            sqlBuilder.append(" AND s.movie_id = :movieId ");
            params.put("movieId", request.getMovieId());
        }
        if (request.getDate() != null) {
            sqlBuilder.append(" AND CAST(s.start_time AS date) = :date ");
            params.put("date", request.getDate());
        }

        String sqlText = SqlUtil.replaceWhere(sqlBuilder.toString());
        String sql = "SELECT s.id, s.room_id, s.movie_id, s.start_time, s.end_time " + sqlText;

        Pair<String, Map<String, Object>> data = SqlUtil.setParam(sql, params);
        responses = jdbcTemplate.query(
                data.getLeft(),
                data.getRight(),
                new BeanPropertyRowMapper<>(ShowtimeItemResponse.class)
        );

        return responses;
    }
}
