package com.healthcore.tenantservice.domain.service;

import com.healthcore.tenantservice.domain.model.vo.Subdomain;

public interface TenantSubdomainGenerator {
    Subdomain generate(String tenantName);
}