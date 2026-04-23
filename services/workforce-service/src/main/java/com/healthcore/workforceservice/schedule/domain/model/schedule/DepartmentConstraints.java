package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.vo.StaffingConstraint;

import java.util.List;

public record DepartmentConstraintAggregate(
        String departmentId,
        List<StaffingConstraint> constraints
) {}