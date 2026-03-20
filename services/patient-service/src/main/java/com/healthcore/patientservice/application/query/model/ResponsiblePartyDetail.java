package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;

import java.util.UUID;

public record ResponsiblePartyDetail(

        UUID id,
        String firstName,
        String lastName,
        String contactNumber,
        String relationship,
        ResponsiblePartyType type,
        AddressDetail address

) {}