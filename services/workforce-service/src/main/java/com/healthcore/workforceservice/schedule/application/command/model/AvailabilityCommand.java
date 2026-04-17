package com.healthcore.workforceservice.schedule.application.command.model;

import java.time.LocalDate;

public record AvailabilityCommand(
    String departmentId,
    LocalDate date,
    int overBookingFactor
) {}
