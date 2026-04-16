package com.healthcore.workforceservice.schedule.application.command.model;

import java.time.LocalDateTime;

public record AssignmentRequest(
        String departmentId,
        LocalDateTime appointmentTime,
        int estimatedDurationMinutes,
        String priorityLevel // NORMAL, URGENT, CRITICAL
) {}