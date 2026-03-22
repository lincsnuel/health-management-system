package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidPatientStatusException extends InvalidOperationException {
    public InvalidPatientStatusException(String message) {
        super(message);
    }
}
