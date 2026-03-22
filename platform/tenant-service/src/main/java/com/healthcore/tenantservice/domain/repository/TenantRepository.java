package com.healthcore.tenantservice.domain.repository;

import com.healthcore.tenantservice.domain.model.tenant.Tenant;

import java.util.Optional;

public interface TenantRepository {

    Tenant save(Tenant tenant);

    Optional<Tenant> findById(String id);
}