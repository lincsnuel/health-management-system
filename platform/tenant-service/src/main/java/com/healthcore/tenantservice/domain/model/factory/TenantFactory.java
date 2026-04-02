package com.healthcore.tenantservice.domain.model.factory;

import com.healthcore.tenantservice.domain.model.tenant.Tenant;
import com.healthcore.tenantservice.domain.model.vo.Subdomain;
import com.healthcore.tenantservice.domain.model.vo.SubscriptionPlanId;
import com.healthcore.tenantservice.domain.model.vo.TenantKey;
import com.healthcore.tenantservice.domain.service.TenantSubdomainGenerator;

public class TenantFactory {

    private final TenantSubdomainGenerator subdomainGenerator;

    public TenantFactory(TenantSubdomainGenerator subdomainGenerator) {
        this.subdomainGenerator = subdomainGenerator;
    }

    public Tenant createWithSubdomain(
            TenantKey tenantKey,
            String name,
            SubscriptionPlanId planId
    ) {
        Subdomain subdomain = subdomainGenerator.generate(name);

        return Tenant.create(
                tenantKey,
                name,
                subdomain,
                planId
        );
    }
}