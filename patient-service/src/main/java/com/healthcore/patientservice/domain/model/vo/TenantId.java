package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidIdException;

public record TenantId(String value) {
    public TenantId {
        if (value == null) {
            throw new InvalidIdException("Tenant ID must be provided");
        }
        value = value.trim().toUpperCase(); // normalization only
    }

    public static TenantId of(String tenantId) {
        return new TenantId(tenantId);
    }
}