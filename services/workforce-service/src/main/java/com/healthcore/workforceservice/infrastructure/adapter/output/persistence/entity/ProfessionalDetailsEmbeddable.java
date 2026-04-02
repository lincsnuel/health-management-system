package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.domain.model.vo.ProfessionalDetails;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * JPA Embeddable for the ProfessionalDetails Value Object.
 * Captures expertise, titles, and consultant status for clinical logic.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfessionalDetailsEmbeddable {

    @Column(name = "prof_specialization", nullable = false)
    private String primarySpecialization;

    @Column(name = "prof_academic_title") // e.g., FWACS, RN, RM
    private String academicTitle;

    @Column(name = "prof_is_consultant", nullable = false)
    private boolean isConsultant;

    /**
     * Maps from Domain VO to JPA Embeddable.
     */
    public static ProfessionalDetailsEmbeddable fromDomain(ProfessionalDetails details) {
        if (details == null) return null;
        return new ProfessionalDetailsEmbeddable(
                details.primarySpecialization(),
                details.academicTitle(),
                details.isConsultant()
        );
    }

    /**
     * Maps from JPA Embeddable back to Domain VO.
     */
    public ProfessionalDetails toDomain() {
        return new ProfessionalDetails(
                primarySpecialization,
                academicTitle,
                isConsultant
        );
    }
}