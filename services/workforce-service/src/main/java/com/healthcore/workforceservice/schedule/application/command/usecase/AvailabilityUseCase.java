package com.healthcore.workforceservice.schedule.application.command.usecase;

import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;

import java.time.LocalDate;
import java.util.Map;

public interface AvailabilityUseCase {

    DepartmentAvailability compute(DepartmentId departmentId, LocalDate date);

    Map<TimeSlot, Integer> computeAsMap(DepartmentId departmentId, LocalDate date);

    void publish(DepartmentId departmentId, LocalDate date);
}