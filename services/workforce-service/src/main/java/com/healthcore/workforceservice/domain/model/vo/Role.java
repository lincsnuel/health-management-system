package com.healthcore.workforceservice.domain.model.vo;

import com.healthcore.workforceservice.domain.exception.DomainException;

/**
 * Value Object representing a functional role within the facility.
 * Examples: "WARD_MANAGER", "CHIEF_MEDICAL_OFFICER", "FRONT_DESK_ADMIN"
 */
public record Role(String value) {

    public Role {
        if (value == null || value.isBlank()) {
            throw new DomainException("Role name cannot be empty");
        }
        // Normalize to uppercase to ensure "Nurse" and "NURSE" are treated as the same role
        value = value.trim().toUpperCase();
    }

    /**
     * Factory method for cleaner creation
     */
    public static Role of(String value) {
        return new Role(value);
    }
}