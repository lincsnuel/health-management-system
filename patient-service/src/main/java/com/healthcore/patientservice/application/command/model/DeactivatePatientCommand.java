package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record DeactivatePatientCommand(
        String tenantId,
        UUID patientId
) {}