package com.healthcore.workforceservice.domain.exception;

public class InvalidPhoneNumberException extends DomainException{
    public InvalidPhoneNumberException(String message) {
        super(message);
    }
}
