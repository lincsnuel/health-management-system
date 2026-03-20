package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import com.healthcore.patientservice.domain.model.enums.PatientStatus;

/**
 * Response DTO for patient summary (lightweight view)
 */
public record PatientSummaryResponse(
        String patientId,
        String hospitalPatientNumber,
        String fullName,
        String gender,
        String dateOfBirth, // ISO-8601 yyyy-MM-dd
        Integer age,
        String contactNumber,
        PatientStatus status
) {}