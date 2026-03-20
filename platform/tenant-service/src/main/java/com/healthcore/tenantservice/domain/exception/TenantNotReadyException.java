package com.healthcore.tenantservice.domain.exception;

public class TenantNotReadyException extends DomainException {
    public TenantNotReadyException(String message) {
        super(message);
    }
}
