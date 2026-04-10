package com.healthcore.appointmentservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record DepartmentId(UUID value) {
    public DepartmentId {
        Objects.requireNonNull(value);
    }
}
