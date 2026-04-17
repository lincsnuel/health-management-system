package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.EmploymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmploymentJpaRepository extends JpaRepository<EmploymentEntity, UUID> {

    Optional<EmploymentEntity> findByStaffId(UUID staffId);

    Optional<EmploymentEntity> findByEmployeeId(String employeeId);

    boolean existsByStaffId(UUID staffId);

    boolean existsByEmployeeId(String employeeId);
}