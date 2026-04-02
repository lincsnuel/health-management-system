package com.healthcore.tenantservice.infrastructure.config;

import com.healthcore.tenantservice.domain.model.factory.TenantFactory;
import com.healthcore.tenantservice.domain.repository.TenantRepository;
import com.healthcore.tenantservice.domain.service.TenantSubdomainGenerator;
import com.healthcore.tenantservice.domain.service.impl.TenantSubdomainGeneratorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantFactoryConfig {

    @Bean
    public TenantSubdomainGenerator tenantSubdomainGenerator(TenantRepository repository) {
        return new TenantSubdomainGeneratorImpl(repository);
    }

    @Bean
    public TenantFactory tenantFactory(TenantSubdomainGenerator generator) {
        return new TenantFactory(generator);
    }
}