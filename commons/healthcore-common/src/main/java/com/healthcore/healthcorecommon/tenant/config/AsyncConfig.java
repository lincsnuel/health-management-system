package com.healthcore.healthcorecommon.tenant.config;

import com.healthcore.healthcorecommon.tenant.context.ContextAwareTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean(name = "contextAwareExecutor")
    public ThreadPoolTaskExecutor executor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);

        executor.setTaskDecorator(new ContextAwareTaskDecorator());

        executor.initialize();
        return executor;
    }
}