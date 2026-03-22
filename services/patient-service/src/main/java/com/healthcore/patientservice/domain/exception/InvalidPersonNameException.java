package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidPersonNameException extends InvalidOperationException {
    public InvalidPersonNameException(String message) {
        super(message);
    }

    public InvalidPersonNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
