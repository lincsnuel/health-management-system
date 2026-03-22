package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidPhoneNumberException extends InvalidOperationException {

    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}