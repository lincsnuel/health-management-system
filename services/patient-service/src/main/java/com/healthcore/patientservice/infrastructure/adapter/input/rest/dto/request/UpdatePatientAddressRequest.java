package com.healthcore.patientservice.infrastructure.adapter.input.rest.dto.request;

public record UpdatePatientAddressRequest(
        String street,
        String city,
        String state,
        String country
) {}