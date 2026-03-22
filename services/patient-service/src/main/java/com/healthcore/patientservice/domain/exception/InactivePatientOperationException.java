package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InactivePatientOperationException extends InvalidOperationException {
    public InactivePatientOperationException(String message) {
        super(message);
    }
}
