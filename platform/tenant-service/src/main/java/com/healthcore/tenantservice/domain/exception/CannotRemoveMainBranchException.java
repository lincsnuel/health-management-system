package com.healthcore.tenantservice.domain.exception;

public class CannotRemoveMainBranchException extends DomainException {
    public CannotRemoveMainBranchException(String message) {
        super(message);
    }
}
