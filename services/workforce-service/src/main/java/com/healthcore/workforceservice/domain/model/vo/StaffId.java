package com.healthcore.workforceservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record StaffId(UUID value) {

    public StaffId {
        Objects.requireNonNull(value, "Patient ID cannot be null");
    }

    public static StaffId of(UUID id) {
        return new StaffId(id);
    }

    public static StaffId newId() {
        return new StaffId(UUID.randomUUID());
    }
}