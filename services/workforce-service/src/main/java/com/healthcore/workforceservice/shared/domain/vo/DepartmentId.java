package com.healthcore.workforceservice.shared.domain.vo;

import java.util.Objects;

public record DepartmentId(String value) {

    public DepartmentId {
        Objects.requireNonNull(value, "Department ID cannot be null");
    }

    public static DepartmentId of(String id) {
        return new DepartmentId(id);
    }
}