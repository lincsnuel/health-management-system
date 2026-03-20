package com.healthcore.patientservice.application.query.model;

import java.util.UUID;

public record PatientContactInfo(
        UUID patientId,
        String fullName,
        String email,
        String phoneNumber
) {}