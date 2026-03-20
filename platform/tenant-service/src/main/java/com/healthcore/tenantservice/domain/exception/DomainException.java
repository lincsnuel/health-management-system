package com.healthcore.tenantservice.domain.exception;

public class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
    protected DomainException(Throwable cause) {
        super(cause);
    }
}
