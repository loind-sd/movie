package com.cinema.showtimeservice.config;

import com.cinema.showtimeservice.job.CleanSearchPrefixJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail cleanSearchPrefixJobDetail() {
        return JobBuilder.newJob(CleanSearchPrefixJob.class)
                .withIdentity("CleanSearchPrefixJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger releaseSeatTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(cleanSearchPrefixJobDetail())
                .withIdentity("cleanSearchPrefixTrigger")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInMinutes(1)
                                .repeatForever()
                )
                .build();
    }
}

