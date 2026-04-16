package com.healthcore.appointmentservice.domain.repository;

import com.healthcore.appointmentservice.domain.model.schedule.DepartmentScheduleProjection;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentScheduleProjectionRepository {

    Optional<DepartmentScheduleProjection> findByTenantAndDepartment(
            UUID tenantId,
            UUID departmentId
    );

    void save(DepartmentScheduleProjection projection);
}