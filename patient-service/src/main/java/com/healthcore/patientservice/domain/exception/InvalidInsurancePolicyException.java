package com.healthcore.patientservice.domain.exception;

public class InvalidInsurancePolicyException extends DomainException {
    public InvalidInsurancePolicyException(String message) {
        super(message);
    }
    public InvalidInsurancePolicyException(String message,  Throwable cause) {
        super(message, cause);
    }
}
