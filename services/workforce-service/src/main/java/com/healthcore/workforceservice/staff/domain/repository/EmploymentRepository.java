package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;

import java.util.Optional;

public interface EmploymentRepository {
    Employment save(Employment employment);

    Optional<Employment> findByStaffId(StaffId staffId);
}