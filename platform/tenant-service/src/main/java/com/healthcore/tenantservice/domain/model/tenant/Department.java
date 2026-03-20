package com.healthcore.tenantservice.domain.model.tenant;

import com.healthcore.tenantservice.domain.exception.InvalidDepartmentException;

import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
public class Department {

    private final UUID id;

    private final String name;

    private final String description;

    private final boolean active;


    private Department(
            UUID id,
            String name,
            String description,
            boolean active
    ) {
        this.id = Objects.requireNonNull(id);
        this.name = normalize(name);
        this.description = normalizeDescription(description);
        this.active = active;
    }


    /* ================= FACTORY ================= */

    public static Department create(
            String name,
            String description
    ) {
        return new Department(
                UUID.randomUUID(),
                name,
                description,
                true
        );
    }


    public static Department reconstruct(
            UUID id,
            String name,
            String description,
            boolean active
    ) {
        return new Department(
                id,
                name,
                description,
                active
        );
    }


    /* ================= STATE TRANSITIONS ================= */

    public Department deactivate() {
        return new Department(
                this.id,
                this.name,
                this.description,
                false
        );
    }

    public Department activate() {
        return new Department(
                this.id,
                this.name,
                this.description,
                true
        );
    }

    public Department rename(String newName) {
        return new Department(
                this.id,
                newName,
                this.description,
                this.active
        );
    }


    public Department updateDescription(String newDescription) {
        return new Department(
                this.id,
                this.name,
                newDescription,
                this.active
        );
    }


    /* ================= VALIDATION ================= */

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidDepartmentException("Department name cannot be blank");
        }
        return value.trim();
    }

    private String normalizeDescription(String value) {
        return value == null ? "" : value.trim();
    }
}