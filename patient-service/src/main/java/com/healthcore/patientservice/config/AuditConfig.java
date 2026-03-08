package com.healthcore.patientservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

public class AuditConfig {
    @Configuration
    @EnableJpaAuditing
    public static class BaseAuditConfig {
        // This stays empty unless you want to add a DateTimeProvider bean
    }
}
