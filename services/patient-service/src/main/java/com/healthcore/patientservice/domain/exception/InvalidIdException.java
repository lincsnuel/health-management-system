package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidIdException extends InvalidOperationException {
    public InvalidIdException(String message) {
        super(message);
    }
}
