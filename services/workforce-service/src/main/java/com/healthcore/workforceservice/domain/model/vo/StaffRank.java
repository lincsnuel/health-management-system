package com.healthcore.workforceservice.domain.model.vo;

import com.healthcore.workforceservice.domain.exception.DomainException;

/**
 * Represents the hierarchical standing of staff.
 * Optional for private clinics, standard for public FMCs/General Hospitals.
 */
public record StaffRank(String cadre, String level) {
    public StaffRank {
        if (cadre == null || cadre.isBlank()) throw new DomainException("Cadre is required");
    }

    public String getDisplayName() {
        return level != null ? cadre + " - Grade " + level : cadre;
    }
}

