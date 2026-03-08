package com.healthcore.patientservice.application.query.model;

import java.util.UUID;

/**
 * Query model representing patient address details.
 * Used for read operations (queries) only.
 */
public record AddressDetail(
        UUID addressId,
        String street,
        String city,
        String state,
        String country,
        boolean primary
) {}