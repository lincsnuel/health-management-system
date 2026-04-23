package com.healthcore.workforceservice.schedule.domain.model.vo;

import com.healthcore.workforceservice.schedule.domain.model.enums.StaffRole;

import java.util.UUID;

public record StaffRecord(
        UUID staffId,
        String departmentId,
        StaffRole role
) {}