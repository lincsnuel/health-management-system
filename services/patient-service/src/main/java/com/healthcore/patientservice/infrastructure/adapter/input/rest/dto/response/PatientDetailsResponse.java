package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

import java.util.List;

/**
 * Response DTO for full patient details (query/read)
 */
public record PatientDetailsResponse(
        String patientId,
        String hospitalPatientNumber,
        String firstName,
        String lastName,
        String fullName,
        String dateOfBirth,   // ISO-8601 yyyy-MM-dd
        Integer age,
        String gender,
        String contactNumber,
        String email,
        String bloodGroup,
        String genotype,
        String religion,
        String identityType,
        String nationalIdNumber,
        List<ResponsiblePartyResponse> responsibleParties,
        List<AddressResponse> addresses,
        List<InsuranceResponse> insurancePolicies,
        String status,
        String createdAt,      // ISO-8601 timestamp
        String updatedAt       // ISO-8601 timestamp
) { }