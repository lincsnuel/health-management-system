package com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response;

import com.healthcore.tenantservice.domain.model.enums.TenantStatus;

import java.util.UUID;
import java.util.List;

public record TenantResponse(
        String id,
        String tenantKey,
        String name,
        String subdomain,
        TenantStatus status,
        boolean setupCompleted,
        UUID subscriptionPlanId,
        List<BranchResponse> branches,
        List<DepartmentResponse> departments
) {}