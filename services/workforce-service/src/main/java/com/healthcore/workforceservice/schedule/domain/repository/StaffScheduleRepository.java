package com.healthcore.workforceservice.schedule.application.query.repository;

import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffScheduleAggregate;

import java.util.UUID;

public interface StaffScheduleRepository {
    StaffScheduleAggregate findByDepartmentId(UUID departmentId);
}