package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidAddressException extends InvalidOperationException {
    public InvalidAddressException(String message) {
        super(message);
    }
}
