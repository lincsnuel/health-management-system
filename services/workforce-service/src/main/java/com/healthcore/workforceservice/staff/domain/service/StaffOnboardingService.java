package com.healthcore.workforceservice.staff.domain.service;

import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;
import com.healthcore.workforceservice.staff.domain.model.vo.Role;

public class StaffOnboardingService {

    public StaffOnboardingResult onboard(
            Staff staff,
            Employment employment,
            ProfessionalProfile profile,
            Credentialing credentialing
    ) {

        // STEP 1: Validate consistency across aggregates
        validateConsistency(staff, employment, profile);

        // STEP 2: Persist coordination happens outside (application layer)
        staff.assignEmployment(employment.getId());
        staff.assignProfessionalProfile(profile.getId());
        staff.assignCredentialing(credentialing.getId());

        // STEP 3: Domain decision - initial role assignment
        staff.assignRole(new Role("STAFF_BASE_ACCESS"));

        return new StaffOnboardingResult(
                staff,
                employment,
                profile,
                credentialing
        );
    }

    private void validateConsistency(
            Staff staff,
            Employment employment,
            ProfessionalProfile profile
    ) {

        if (!staff.getStaffId().equals(employment.getStaffId())) {
            throw new IllegalStateException("Staff-Employment mismatch");
        }

        if (!staff.getStaffId().equals(profile.getStaffId())) {
            throw new IllegalStateException("Staff-Profile mismatch");
        }
    }
}