package com.healthcore.patientservice.application.exception;

import com.healthcore.healthcorecommon.exception.base.NotFoundException;

public class PatientNotFoundException extends NotFoundException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
