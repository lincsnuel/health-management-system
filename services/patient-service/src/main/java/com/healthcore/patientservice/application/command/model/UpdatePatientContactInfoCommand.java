package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record UpdatePatientContactInfoCommand(
        UUID patientId,
        String phoneNumber,
        String email
) {}