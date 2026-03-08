package com.healthcore.patientservice.domain.exception;

public class InvalidNationalIdentityException extends DomainException {
    public InvalidNationalIdentityException(String message) {
        super(message);
    }

    public InvalidNationalIdentityException(String message, Throwable cause) {
        super(message,  cause);
    }
}
