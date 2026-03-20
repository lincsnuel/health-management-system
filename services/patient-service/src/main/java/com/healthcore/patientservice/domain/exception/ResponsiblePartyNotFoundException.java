package com.healthcore.patientservice.domain.exception;

import com.healthcore.patientservice.application.exception.ApplicationException;

public class ResponsiblePartyNotFoundException extends ApplicationException {
    public ResponsiblePartyNotFoundException(String message) {
        super(message);
    }
}
