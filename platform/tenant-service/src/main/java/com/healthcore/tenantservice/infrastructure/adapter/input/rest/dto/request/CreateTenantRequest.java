package com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.request;

import java.util.UUID;

public record CreateTenantRequest(
        UUID tenantKey,
        String name,
        String subscriptionPlanId
) {
}