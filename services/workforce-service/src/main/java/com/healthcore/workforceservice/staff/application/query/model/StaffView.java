package com.healthcore.workforceservice.staff.application.query.model;

import com.healthcore.workforceservice.staff.domain.model.enums.StaffStatus;

import java.util.UUID;

public record StaffView(
        UUID staffId,
        String fullName,
        String email,
        String departmentName,
        StaffStatus status
) {}