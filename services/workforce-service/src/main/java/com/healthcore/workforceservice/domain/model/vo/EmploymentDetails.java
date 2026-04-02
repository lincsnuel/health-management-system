package com.healthcore.workforceservice.domain.model.vo;

import com.healthcore.workforceservice.domain.exception.DomainException;
import com.healthcore.workforceservice.domain.model.enums.EmploymentType;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable Value Object representing facility-specific employment data.
 */
public record EmploymentDetails(
        String employeeId,       // The internal ID assigned by the hospital (e.g., GH/MED/001)
        EmploymentType type,     // FULL_TIME, PART_TIME, CONTRACT, LOCUM
        LocalDate dateHired
) {
    public EmploymentDetails {
        if (employeeId == null || employeeId.isBlank()) {
            throw new DomainException("Employee ID is required");
        }
        Objects.requireNonNull(type, "Employment type is required");
        Objects.requireNonNull(dateHired, "Hiring date is required");

        if (dateHired.isAfter(LocalDate.now())) {
            throw new DomainException("Hiring date cannot be in the future");
        }
    }
}