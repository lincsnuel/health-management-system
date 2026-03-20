package com.healthcore.tenantservice.domain.exception;

public class InvalidTenantStateException extends DomainException {
    public InvalidTenantStateException(String message) {
        super(message);
    }
}
