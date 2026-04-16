package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;

public interface ProfessionalProfileRepository {
    ProfessionalProfile save(ProfessionalProfile profile);
}