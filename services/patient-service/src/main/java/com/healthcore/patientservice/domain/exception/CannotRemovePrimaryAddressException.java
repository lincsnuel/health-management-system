package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class CannotRemovePrimaryAddressException extends InvalidOperationException {
    public CannotRemovePrimaryAddressException(String message) {
        super(message);
    }
}
