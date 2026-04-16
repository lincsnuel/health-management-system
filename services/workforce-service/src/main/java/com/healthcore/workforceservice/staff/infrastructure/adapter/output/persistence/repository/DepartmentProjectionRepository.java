package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.DepartmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentProjectionRepository
        extends JpaRepository<DepartmentProjection, String> {

    boolean existsByDepartmentId(String departmentId);
}