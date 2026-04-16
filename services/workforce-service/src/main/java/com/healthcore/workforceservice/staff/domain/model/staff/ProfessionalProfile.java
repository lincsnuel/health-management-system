package com.healthcore.workforceservice.staff.domain.model.staff;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.exception.DomainException;
import com.healthcore.workforceservice.staff.domain.model.vo.ProfessionalProfileId;
import lombok.Getter;

@Getter
public class ProfessionalProfile {

    private final ProfessionalProfileId id;
    private final StaffId staffId;

    private String primarySpecialization;
    private final String academicTitle;
    private boolean consultant;

    private ProfessionalProfile(
            ProfessionalProfileId id,
            StaffId staffId,
            String specialization,
            String title,
            boolean consultant
    ) {
        this.id = id;
        this.staffId = staffId;
        this.primarySpecialization = specialization;
        this.academicTitle = title;
        this.consultant = consultant;
    }

    public static ProfessionalProfile create(
            ProfessionalProfileId id,
            StaffId staffId,
            String specialization,
            String title,
            boolean consultant
    ) {
        if (specialization == null || specialization.isBlank()) {
            throw new DomainException("Specialization required");
        }

        return new ProfessionalProfile(id, staffId, specialization.trim(), title, consultant);
    }

    public void updateSpecialization(String specialization) {
        this.primarySpecialization = specialization.trim();
    }

    public void promoteToConsultant() {
        this.consultant = true;
    }
}