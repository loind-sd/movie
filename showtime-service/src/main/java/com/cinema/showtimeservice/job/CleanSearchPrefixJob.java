package com.cinema.showtimeservice.job;

import com.cinema.common.constants.CommonConstants;
import com.cinema.common.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@DisallowConcurrentExecution
@RequiredArgsConstructor
public class CleanSearchPrefixJob implements Job {
    private final RedisService redisService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        redisService.keepZSetTopN(CommonConstants.RedisKey.PREFIX_SUGGESTION, 10);
        log.info("[QUARTZ_JOB] Cleaned search prefix suggestions, kept top 10 entries.");
    }
}
