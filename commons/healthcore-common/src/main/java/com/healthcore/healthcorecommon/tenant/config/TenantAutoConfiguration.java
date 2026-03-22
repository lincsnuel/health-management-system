package com.healthcore.healthcorecommon.tenant.config;

import com.healthcore.healthcorecommon.tenant.persistence.TenantAspect;
import com.healthcore.healthcorecommon.tenant.persistence.TenantHibernateInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TenantAutoConfiguration {

    @Bean
    public TenantHibernateInterceptor tenantHibernateInterceptor() {
        return new TenantHibernateInterceptor();
    }

    @Bean
    public TenantAspect tenantAspect(TenantHibernateInterceptor interceptor) {
        return new TenantAspect(interceptor);
    }
}