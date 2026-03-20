package com.healthcore.tenantservice.domain.exception;

public class BranchLimitExceededException extends DomainException {
    public BranchLimitExceededException(String message) {
        super(message);
    }
}
