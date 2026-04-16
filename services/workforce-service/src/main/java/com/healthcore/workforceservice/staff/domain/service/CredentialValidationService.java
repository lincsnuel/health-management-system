package com.healthcore.workforceservice.staff.domain.service;

import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;

public class CredentialValidationService {

    public boolean isEligibleForClinicalWork(
            Staff staff,
            Credentialing credentialing
    ) {

        if (!isMedicalStaff(staff)) {
            return true; // non-clinical staff don't need licenses
        }

        return credentialing.hasValidLicense();
    }

    public void enforceEligibility(
            Staff staff,
            Credentialing credentialing
    ) {
        if (!isEligibleForClinicalWork(staff, credentialing)) {
            throw new IllegalStateException(
                    "Staff is not eligible for clinical operations"
            );
        }
    }

    private boolean isMedicalStaff(Staff staff) {
        return staff.getStaffType() == StaffType.DOCTOR
                || staff.getStaffType() == StaffType.NURSE
                || staff.getStaffType() == StaffType.PHARMACIST
                || staff.getStaffType() == StaffType.LAB_SCIENTIST;
    }
}