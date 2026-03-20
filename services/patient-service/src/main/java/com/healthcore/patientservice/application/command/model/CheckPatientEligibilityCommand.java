package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record CheckPatientEligibilityCommand(
        UUID patientId,
        String tenantId,
        int minAge,
        int maxAge
) {}