package com.healthcore.tenantservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record TenantId(UUID value) {

    public TenantId {
        Objects.requireNonNull(value, "TenantId value must not be null");
    }

    public static TenantId newId() {
        return new TenantId(UUID.randomUUID());
    }
}