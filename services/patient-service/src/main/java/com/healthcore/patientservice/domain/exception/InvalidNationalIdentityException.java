package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidNationalIdentityException extends InvalidOperationException {
    public InvalidNationalIdentityException(String message) {
        super(message);
    }

    public InvalidNationalIdentityException(String message, Throwable cause) {
        super(message,  cause);
    }
}
