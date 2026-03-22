package com.healthcore.tenantservice.application.usecase;

import com.healthcore.tenantservice.domain.model.tenant.Tenant;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.TenantResponse;

import java.util.UUID;

public interface QueryTenantUseCase {
    TenantResponse findByTenantId(String tenantId);
}
