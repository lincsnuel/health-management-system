package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

/**
 * Response DTO for patient name (query/read)
 */
public record PatientNameResponse(
        String patientId,                // UUID as String
        String hospitalPatientNumber,
        String fullName
) {}