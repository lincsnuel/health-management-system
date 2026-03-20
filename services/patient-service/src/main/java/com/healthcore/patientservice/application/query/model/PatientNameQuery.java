package com.healthcore.patientservice.application.query.model;

import java.util.UUID;

/**
 * Query model for patient name
 */
public record PatientNameQuery(
        UUID patientId,
        String hospitalPatientNumber,
        String fullName
) {}