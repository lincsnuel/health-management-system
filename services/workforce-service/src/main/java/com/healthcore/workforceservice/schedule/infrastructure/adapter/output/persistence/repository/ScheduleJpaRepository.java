package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, UUID> {

    Optional<ScheduleEntity> findByDepartmentId(UUID departmentId);

    boolean existsByDepartmentId(UUID departmentId);
}