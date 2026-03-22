package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidInsurancePolicyException extends InvalidOperationException {
    public InvalidInsurancePolicyException(String message) {
        super(message);
    }
    public InvalidInsurancePolicyException(String message,  Throwable cause) {
        super(message, cause);
    }
}
