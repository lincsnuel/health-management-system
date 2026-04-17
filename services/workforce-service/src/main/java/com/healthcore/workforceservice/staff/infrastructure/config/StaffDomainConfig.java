package com.healthcore.workforceservice.staff.infrastructure.config;

import com.healthcore.workforceservice.staff.domain.service.CredentialValidationService;
import com.healthcore.workforceservice.staff.domain.service.StaffActivationService;
import com.healthcore.workforceservice.staff.domain.service.StaffOnboardingService;
import com.healthcore.workforceservice.staff.domain.service.StaffTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StaffDomainConfig {

    @Bean
    public CredentialValidationService credentialValidationService() {
        return new CredentialValidationService();
    }

    @Bean
    public StaffActivationService staffActivationService() {
        return new StaffActivationService();
    }

    @Bean
    public StaffOnboardingService staffOnboardingService() {
        return new StaffOnboardingService();
    }

    @Bean
    public StaffTransferService staffTransferService() {
        return new StaffTransferService();
    }
}