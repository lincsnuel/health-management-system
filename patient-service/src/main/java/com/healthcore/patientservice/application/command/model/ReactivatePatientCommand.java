package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record ReactivatePatientCommand(
        String tenantId,
        UUID patientId
) {}