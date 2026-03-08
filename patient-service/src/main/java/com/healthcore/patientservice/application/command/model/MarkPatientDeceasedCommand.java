package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record MarkPatientDeceasedCommand(
        String tenantId,
        UUID patientId
) {}