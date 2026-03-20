package com.healthcore.tenantservice.domain.exception;

public class InvalidTenantException extends DomainException {
    public InvalidTenantException(String message) {
        super(message);
    }
}
