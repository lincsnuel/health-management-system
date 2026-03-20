package com.healthcore.tenantservice.domain.exception;

public class BranchNotFoundException extends DomainException {
    public BranchNotFoundException(String message) {
        super(message);
    }
}
