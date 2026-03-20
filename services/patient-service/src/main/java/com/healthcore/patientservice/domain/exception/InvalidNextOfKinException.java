package com.healthcore.patientservice.domain.exception;

public class InvalidNextOfKinException extends DomainException {
    public InvalidNextOfKinException(String message) {
        super(message);
    }

    public InvalidNextOfKinException(String message, Throwable cause) {
        super(message, cause);
    }
}
