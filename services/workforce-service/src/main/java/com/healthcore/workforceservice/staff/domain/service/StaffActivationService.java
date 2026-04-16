package com.healthcore.workforceservice.staff.domain.service;

import com.healthcore.workforceservice.staff.domain.model.enums.StaffType;
import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;

public class StaffActivationService {

    public void activateStaff(
            Staff staff,
            Credentialing credentialing
    ) {

        // CROSS-AGGREGATE INVARIANTS

        if (!credentialing.hasValidLicense()
                && isMedicalStaff(staff)) {
            throw new IllegalStateException(
                    "Medical staff cannot be activated without valid license"
            );
        }

        if (staff.getRoles().isEmpty()) {
            throw new IllegalStateException("At least one role required");
        }

        staff.activate();
    }

    private boolean isMedicalStaff(Staff staff) {
        return staff.getStaffType() == StaffType.DOCTOR
                || staff.getStaffType() == StaffType.NURSE
                || staff.getStaffType() == StaffType.PHARMACIST
                || staff.getStaffType() == StaffType.LAB_SCIENTIST;
    }
}