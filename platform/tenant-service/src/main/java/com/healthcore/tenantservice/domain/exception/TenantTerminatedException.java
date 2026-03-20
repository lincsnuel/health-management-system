package com.healthcore.tenantservice.domain.exception;

public class TenantTerminatedException extends DomainException {
    public TenantTerminatedException(String message) {
        super(message);
    }
}
