package com.healthcore.healthcorecommon.tenant.config;

import com.healthcore.healthcorecommon.tenant.filter.RequestContextFilter;
import com.healthcore.healthcorecommon.tenant.persistence.TenantIdentifierResolver;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantAutoConfiguration {

    @Bean
    public RequestContextFilter tenantRequestContextFilter() {
        return new RequestContextFilter();
    }

    @Bean
    public CurrentTenantIdentifierResolver<String> tenantIdentifierResolver() {
        return new TenantIdentifierResolver();
    }
}