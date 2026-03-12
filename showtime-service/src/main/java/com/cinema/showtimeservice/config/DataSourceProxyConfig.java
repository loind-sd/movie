package com.cinema.showtimeservice.config;

import com.cinema.showtimeservice.listener.CustomSlowQueryListener;
import io.micrometer.core.instrument.MeterRegistry;
import net.ttddyy.dsproxy.listener.SlowQueryListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceProxyConfig {

    private final MeterRegistry meterRegistry;

    public DataSourceProxyConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Bean
    public BeanPostProcessor dataSourcePostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) {

                if (bean instanceof DataSource) {
                    return ProxyDataSourceBuilder
                            .create((DataSource) bean)
                            .name("DS")
                            .listener(new CustomSlowQueryListener(meterRegistry))
                            .build();
                }

                return bean;
            }
        };
    }
}
