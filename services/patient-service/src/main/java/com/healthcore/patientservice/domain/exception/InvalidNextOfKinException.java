package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidNextOfKinException extends InvalidOperationException {
    public InvalidNextOfKinException(String message) {
        super(message);
    }

    public InvalidNextOfKinException(String message, Throwable cause) {
        super(message, cause);
    }
}
