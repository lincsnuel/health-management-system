package com.healthcore.patientservice.application.command.model;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;

public record ResponsiblePartyCommand(

        String firstName,
        String lastName,
        String contactNumber,
        String relationship,
        ResponsiblePartyType type,
        AddressCommand address

) {}