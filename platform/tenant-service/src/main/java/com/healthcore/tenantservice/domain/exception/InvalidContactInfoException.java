package com.healthcore.tenantservice.domain.exception;

public class InvalidContactInfoException extends DomainException {
    public InvalidContactInfoException(String message) {
        super(message);
    }

    public InvalidContactInfoException(Throwable cause) {
        super(cause);
    }
}
