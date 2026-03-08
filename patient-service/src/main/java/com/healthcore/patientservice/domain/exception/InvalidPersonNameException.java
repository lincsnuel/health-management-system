package com.healthcore.patientservice.domain.exception;

public class InvalidPersonNameException extends DomainException {
    public InvalidPersonNameException(String message) {
        super(message);
    }

    public InvalidPersonNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
