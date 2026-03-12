package com.cinema.showtimeservice.listener;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.proxy.ParameterSetOperation;
import org.bouncycastle.util.Strings;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomSlowQueryListener implements QueryExecutionListener {

    private final MeterRegistry meterRegistry;

    @Override
    public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {}

    @Override
    public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList) {

        long time = execInfo.getElapsedTime();

        if (time > 1000) {

            String sql = queryInfoList.stream()
                    .map(QueryInfo::getQuery)
                    .collect(Collectors.joining("; "));

            Counter.builder("jpa.repository.slow.full")
                    .tag("sql", sanitize(sql))
                    .register(meterRegistry)
                    .increment();
        }

        Metrics.timer("sql.execution")
                .record(time, TimeUnit.MILLISECONDS);
    }

    private String sanitize(String sql) {
        return sql
                .replaceAll("\\s+", " ")
                .replaceAll("\\d+", "?");
    }
}
