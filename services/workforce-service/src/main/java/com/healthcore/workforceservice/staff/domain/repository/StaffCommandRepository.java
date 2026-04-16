package com.healthcore.workforceservice.staff.domain.repository;

import com.healthcore.workforceservice.staff.domain.model.staff.Staff;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface StaffCommandRepository {

    Staff save(Staff staff);

    Optional<Staff> findById(UUID staffId,  UUID tenantId);

    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmployeeId(String employeeId);

    boolean existsByNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );
}