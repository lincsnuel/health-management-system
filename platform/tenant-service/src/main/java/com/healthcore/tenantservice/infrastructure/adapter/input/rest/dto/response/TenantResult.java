package com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response;

public record TenantResult(
        String id,
        String tenantKey,
        String name,
        String subdomain,
        String status
) {
}