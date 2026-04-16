package com.healthcore.workforceservice.schedule.domain.repository;

import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.ScheduleId;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;

import java.util.Optional;

public interface ScheduleRepository {

    Optional<Schedule> findById(ScheduleId scheduleId);

    Optional<Schedule> findByDepartmentId(DepartmentId departmentId);

    void save(Schedule schedule);

    boolean existsByDepartmentId(DepartmentId departmentId);
}