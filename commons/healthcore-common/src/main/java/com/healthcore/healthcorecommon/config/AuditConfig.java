package com.healthcore.healthcorecommon.config;

import com.healthcore.healthcorecommon.audit.RequestContextAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public RequestContextAuditorAware auditorProvider() {
        return new RequestContextAuditorAware();
    }
}