package com.healthcore.patientservice.application.command.model;

import java.util.UUID;

public record UpdatePatientAddressCommand(
        UUID addressId,
        UUID patientId,
        String tenantId,
        String street,
        String city,
        String state,
        String country,
        boolean primary
) {}