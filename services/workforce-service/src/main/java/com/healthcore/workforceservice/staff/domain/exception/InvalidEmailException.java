package com.healthcore.workforceservice.staff.domain.exception;

public class InvalidEmailException extends DomainException{
    public InvalidEmailException(String message) {
        super(message);
    }
}
