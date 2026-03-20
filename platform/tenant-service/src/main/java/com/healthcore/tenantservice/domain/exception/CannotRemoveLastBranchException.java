package com.healthcore.tenantservice.domain.exception;

public class CannotRemoveLastBranchException extends DomainException {
    public CannotRemoveLastBranchException(String message) {
        super(message);
    }
}
