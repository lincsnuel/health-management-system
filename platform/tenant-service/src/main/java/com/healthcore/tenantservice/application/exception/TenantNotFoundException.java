package com.healthcore.tenantservice.application.exception;

public class TenantNotFoundException extends ApplicationException {
    public TenantNotFoundException(String message) {
        super(message);
    }
}
