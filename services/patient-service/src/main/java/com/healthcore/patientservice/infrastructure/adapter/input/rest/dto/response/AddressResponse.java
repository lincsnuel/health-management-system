package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.response;

/**
 * Response DTO for patient address (query/read)
 */
public record AddressResponse(
        String addressId,
        String street,
        String city,
        String state,
        String country,
        boolean primary
) {}