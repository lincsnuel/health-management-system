package com.healthcore.workforceservice.staff.domain.model.vo;

import com.healthcore.workforceservice.staff.domain.exception.DomainException;

/**
 * Value Object describing the staff's professional standing.
 * Specialization is mandatory for Consultants and Specialist Nurses.
 */
public record ProfessionalDetails(
        String primarySpecialization, // e.g., "Cardiology", "Pediatrics", "General Practice"
        String academicTitle,         // e.g., "FWACS", "MD", "PhD", "RN/RM"
        boolean isConsultant          // Critical for billing and referral logic
) {
    public ProfessionalDetails {
        if (primarySpecialization == null || primarySpecialization.isBlank()) {
            throw new DomainException("Primary specialization is required for professional profiling");
        }

        // Normalize for consistent searching/filtering
        primarySpecialization = primarySpecialization.trim();
    }

    /**
     * Helper to check if the staff is a specialist.
     */
    public boolean isSpecialist() {
        return isConsultant || !primarySpecialization.equalsIgnoreCase("General Practice");
    }
}