package com.healthcore.workforceservice.staff.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record EmploymentId(UUID value) {

    public EmploymentId {
        Objects.requireNonNull(value, "Employment ID cannot be null");
    }

    public static EmploymentId of(UUID id) {
        return new EmploymentId(id);
    }

    public static EmploymentId newId() {return new EmploymentId(UUID.randomUUID());}
}