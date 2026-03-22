package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.NotFoundException;

public class ResponsiblePartyNotFoundException extends NotFoundException {
    public ResponsiblePartyNotFoundException(String message) {
        super(message);
    }
}
