package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

/**
 * Response DTO for patient insurance details (query/read)
 */
public record InsuranceResponse(
        String insuranceId,
        String providerName,
        String policyNumber,
        String planType,
        String coverageStart, // ISO-8601 yyyy-MM-dd
        String coverageEnd,   // ISO-8601 yyyy-MM-dd
        boolean main,
        boolean active
) {}