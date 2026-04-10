package com.healthcore.appointmentservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record TenantId(UUID value) {
    public TenantId {
        Objects.requireNonNull(value);
    }
}