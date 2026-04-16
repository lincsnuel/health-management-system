package com.healthcore.workforceservice.shared.domain.vo;

import java.util.Objects;
import java.util.UUID;

public record StaffId(UUID value) {

    public StaffId {
        Objects.requireNonNull(value, "Staff ID cannot be null");
    }

    public static StaffId of(UUID id) {
        return new StaffId(id);
    }
}