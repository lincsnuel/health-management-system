package com.healthcore.patientservice.domain.exception;

public class InvalidPhoneNumberException extends DomainException {

    public InvalidPhoneNumberException(String message) {
        super(message);
    }

    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }
}