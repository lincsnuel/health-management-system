package com.healthcore.patientservice.application.query.model;

import java.time.LocalDate;
import java.util.UUID;

public record EligiblePatientProjection(
        UUID patientId,
        String firstName,
        String lastName,
        LocalDate dateOfBirth
) {}