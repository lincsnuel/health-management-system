package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.ProfessionalProfile;
import com.healthcore.workforceservice.staff.domain.model.vo.ProfessionalProfileId;

import java.util.Optional;

public interface ProfessionalProfileRepository {
    Optional<ProfessionalProfile> findById(ProfessionalProfileId id);
    Optional<ProfessionalProfile> findByStaffId(StaffId staffId);
    void save(ProfessionalProfile profile);
}