package com.healthcore.workforceservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record DepartmentId(String value) {

    public DepartmentId {
        Objects.requireNonNull(value, "Department ID cannot be null");
    }

    public static DepartmentId of(String id) {
        return new DepartmentId(id);
    }
}