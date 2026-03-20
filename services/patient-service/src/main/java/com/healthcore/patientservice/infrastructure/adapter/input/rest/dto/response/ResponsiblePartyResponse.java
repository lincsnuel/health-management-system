package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

/**
 * Response DTO for patient responsible party / next of kin
 */
public record ResponsiblePartyResponse(
        String id,
        String firstName,
        String lastName,
        String contactNumber,
        String relationship,
        String type,             // ResponsiblePartyType as String
        AddressResponse address
) {}