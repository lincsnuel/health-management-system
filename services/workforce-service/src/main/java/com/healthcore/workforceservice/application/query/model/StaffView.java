package com.healthcore.workforceservice.application.query.model;

import java.util.UUID;

public record StaffView(
        UUID staffId,
        String fullName,
        String email,
        String departmentName,
        String status
) {}