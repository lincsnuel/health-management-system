package com.healthcore.patientservice.application.query.model;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import com.healthcore.patientservice.domain.model.vo.Address;

public record ResponsiblePartyDetail(

        String firstName,
        String lastName,
        String contactNumber,
        String relationship,
        ResponsiblePartyType type,
        AddressDetail address

) {}