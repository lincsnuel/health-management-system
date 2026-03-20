package com.healthcore.tenantservice.domain.exception;

public class IncompleteSetupException extends DomainException {
    public IncompleteSetupException(String message) {
        super(message);
    }
}
