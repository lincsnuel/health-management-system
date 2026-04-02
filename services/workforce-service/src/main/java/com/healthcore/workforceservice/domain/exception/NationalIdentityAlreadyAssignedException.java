package com.healthcore.workforceservice.domain.exception;

public class NationalIdentityAlreadyAssignedException extends DomainException {
    public NationalIdentityAlreadyAssignedException(String message) {
        super(message);
    }
}
