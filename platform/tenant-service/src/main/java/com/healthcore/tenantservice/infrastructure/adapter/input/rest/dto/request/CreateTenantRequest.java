package com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.request;

public record CreateTenantRequest(
        String tenantKey,
        String name,
        String subscriptionPlanId
) {
}