package com.cinema.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlUtil {
    public static Pair<String, Map<String, Object>> setParamWithPageable(
            String sql,
            Map<String, Object> params,
            Pageable pageable
    ) {
        StringBuilder sqlBuilder = new StringBuilder(sql);

        // Thêm sort nếu có
        if (pageable.getSort().isSorted()) {
            sqlBuilder.append(" ORDER BY ");
            String orderBy = pageable.getSort().stream()
                    .map(order -> order.getProperty() + " " + order.getDirection().name())
                    .collect(Collectors.joining(", "));
            sqlBuilder.append(orderBy);
        }

        // Thêm limit và offset
        sqlBuilder.append(" LIMIT :limit OFFSET :offset ");

        Map<String, Object> pagingParams = new HashMap<>(params);
        pagingParams.put("limit", pageable.getPageSize());
        pagingParams.put("offset", pageable.getOffset());

        return Pair.of(sqlBuilder.toString(), pagingParams);
    }

    public static Pair<String, Map<String, Object>> setParam(String sql, Map<String, Object> params) {
        return Pair.of(sql, params);
    }

    public static String replaceWhere(String sql) {
        if (StringUtils.isBlank(sql)) {
            return sql;
        }
        // [where 1 = 1 and] --> [where]
        sql = sql.replaceAll("(?i)\\bWHERE\\s+1\\s+=\\s+1\\s+AND\\b", " WHERE ");
        // remove [where 1 = 1]
        sql = sql.replaceAll("(?i)\\bWHERE\\s+1\\s+=\\s+1\\b", "");
        return sql.trim();
    }

    public static String getValueSelectLike(String value) {
        return "%" + value + "%";
    }

    public static Pair<StringBuilder, Map<String, Object>> whereDateRange(
            StringBuilder sql,
            Map<String, Object> params,
            String columnName,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        sql.append(" AND ").append(columnName).append(" between :fromDate and :toDate ");
        params.put("fromDate", fromDate);
        params.put("toDate", toDate.plusDays(1));
        return Pair.of(sql, params);
    }
}
