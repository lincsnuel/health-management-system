package com.healthcore.patientservice.application.command.model;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;

import java.util.UUID;

public record UpdateResponsiblePartyCommand(
        UUID responsiblePartyId,
        UUID patientId,
        String tenantId,
        String firstName,
        String lastName,
        String phoneNumber,
        String relationship,
        ResponsiblePartyType type,
        UUID addressId,
        String street,
        String city,
        String state,
        String country,
        boolean primary
) {}