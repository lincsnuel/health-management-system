package com.healthcore.workforceservice.staff.domain.exception;

public class DomainException extends RuntimeException {
    public DomainException() {
    }
    public DomainException(String message) {
        super(message);
    }
}
