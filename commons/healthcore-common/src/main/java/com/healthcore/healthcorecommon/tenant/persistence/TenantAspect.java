package com.healthcore.healthcorecommon.tenant.persistence;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class TenantAspect {

    private final TenantHibernateInterceptor interceptor;

    public TenantAspect(TenantHibernateInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Before("execution(* com.healthcore..repository..*(..))")
    public void beforeRepositoryCall() {
        interceptor.enableFilter();
    }
}