package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.model.staff.Employment;
import com.healthcore.workforceservice.staff.domain.model.vo.EmploymentId;

import java.util.Optional;

public interface EmploymentRepository {
    Optional<Employment> findById(EmploymentId id);
    Optional<Employment> findByStaffId(StaffId staffId);
    void save(Employment employment);
}