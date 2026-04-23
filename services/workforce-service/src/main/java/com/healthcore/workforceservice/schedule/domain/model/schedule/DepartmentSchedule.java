package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.vo.Shift;

import java.util.List;

public record DepartmentSchedule(
        String departmentId,
        List<Shift> shifts,
        ShiftPattern defaultPattern
) {}