package com.healthcore.workforceservice.staff.domain.model.vo;

import java.util.Objects;

public record FullName(String firstName, String lastName, String middleName) {
    public FullName {
        Objects.requireNonNull(firstName, "First name required");
        Objects.requireNonNull(lastName, "Last name required");
    }

    public String getFullDisplayName() {
        return middleName != null ?
                String.format("%s %s %s", firstName, middleName, lastName) :
                String.format("%s %s", firstName, lastName);
    }
}