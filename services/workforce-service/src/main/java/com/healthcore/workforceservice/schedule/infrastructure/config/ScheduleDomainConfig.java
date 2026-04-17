package com.healthcore.workforceservice.schedule.infrastructure.config;

import com.healthcore.workforceservice.schedule.domain.service.AvailabilityPolicyDomainService;
import com.healthcore.workforceservice.schedule.domain.service.SchedulingEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleDomainConfig {

    @Bean
    public SchedulingEngine schedulingEngine() {
        return new SchedulingEngine();
    }

    @Bean
    public AvailabilityPolicyDomainService  availabilityPolicyDomainService() {
        return new AvailabilityPolicyDomainService();
    }
}