package com.healthcore.workforceservice.domain.repository;

import com.healthcore.workforceservice.domain.model.staff.Staff;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface StaffCommandRepository {

    Staff save(Staff staff);

    Optional<Staff> findById(UUID staffId,  UUID tenantId);

    Optional<Staff> findByEmail(String tenantId, String email);

    boolean existsByEmail(String tenantId, String email);

    boolean existsByEmployeeId(String tenantId, String employeeId);

    boolean existsByTenantIdAndNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );
}