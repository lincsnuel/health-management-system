package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class AddressLimitExceededException extends InvalidOperationException {
    public AddressLimitExceededException(String message) {
        super(message);
    }
}
