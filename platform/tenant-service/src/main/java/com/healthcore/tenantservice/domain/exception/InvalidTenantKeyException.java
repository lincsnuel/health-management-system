package com.healthcore.tenantservice.domain.exception;

public class InvalidTenantKeyException extends DomainException {
    public InvalidTenantKeyException(String message) {
        super(message);
    }
}
