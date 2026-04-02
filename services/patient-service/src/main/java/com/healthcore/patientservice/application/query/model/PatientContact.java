package com.healthcore.patientservice.application.query.model;

import java.util.UUID;

public record PatientContact(
        UUID patientId,
        UUID tenantId,
        String phoneNumber,
        String email
) {}