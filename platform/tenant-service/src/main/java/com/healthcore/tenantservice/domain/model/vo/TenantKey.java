package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidTenantKeyException;

import java.util.Objects;
import java.util.UUID;

public record TenantKey(UUID value) {

    public TenantKey {
        if (value == null) {
            throw new InvalidTenantKeyException("TenantKey must not be blank");
        }
    }

    /**
     * Generate a new random tenant key (e.g., for activation or API access)
     */
    public static TenantKey newKey() {
        // Use UUID as base — can replace with more complex secure key later
        return new TenantKey(UUID.randomUUID());
    }
}