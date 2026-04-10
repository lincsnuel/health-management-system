package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record CheckPatientEligibilityCommand(
        UUID patientId,
        int minAge,
        int maxAge
) {}