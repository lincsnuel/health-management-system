package com.healthcore.workforceservice.staff.domain.service;

import com.healthcore.workforceservice.staff.domain.model.staff.Credentialing;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;
import com.healthcore.workforceservice.staff.domain.model.staff.Staff;

public record StaffOnboardingResult(
        Staff staff,
        Employment employment,
        ProfessionalProfile profile,
        Credentialing credentialing
) {}