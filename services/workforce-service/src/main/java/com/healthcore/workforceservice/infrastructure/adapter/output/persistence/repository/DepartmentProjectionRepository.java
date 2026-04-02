package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity.DepartmentProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentProjectionRepository
        extends JpaRepository<DepartmentProjection, String> {

    boolean existsByTenantIdAndDepartmentId(String tenantId, String departmentId);
}