package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import com.healthcore.patientservice.domain.model.enums.PatientStatus;

/**
 * Response DTO for lightweight patient listing
 */
public record PatientListItemResponse(
        String patientId,
        String hospitalPatientNumber,
        String firstName,
        String lastName,
        String fullName,
        String dateOfBirth,  // ISO-8601 yyyy-MM-dd
        Integer age,
        String gender,
        String contactNumber,
        PatientStatus status
) {}