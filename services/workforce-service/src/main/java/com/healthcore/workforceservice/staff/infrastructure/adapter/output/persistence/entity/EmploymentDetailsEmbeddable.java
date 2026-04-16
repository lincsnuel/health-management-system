package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.staff.domain.model.enums.EmploymentType;
import com.healthcore.workforceservice.staff.domain.model.vo.EmploymentDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * JPA Embeddable for the EmploymentDetails Value Object.
 * Captures facility-specific hiring data.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmploymentDetailsEmbeddable {

    @Column(name = "employee_facility_id") // The hospital's own ID for the staff
    private String employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type")
    private EmploymentType type;

    @Column(name = "date_hired")
    private LocalDate dateHired;

    /**
     * Maps from Domain VO to JPA Embeddable.
     */
    public static EmploymentDetailsEmbeddable fromDomain(EmploymentDetails details) {
        if (details == null) return null;
        return new EmploymentDetailsEmbeddable(
                details.employeeId(),
                details.type(),
                details.dateHired()
        );
    }

    /**
     * Maps from JPA Embeddable back to Domain VO.
     */
    public EmploymentDetails toDomain() {
        return new EmploymentDetails(employeeId, type, dateHired);
    }
}