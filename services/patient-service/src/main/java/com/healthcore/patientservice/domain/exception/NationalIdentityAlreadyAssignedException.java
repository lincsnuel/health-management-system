package com.healthcore.patientservice.domain.exception;

public class NationalIdentityAlreadyAssignedException extends DomainException {
    public NationalIdentityAlreadyAssignedException(String message) {
        super(message);
    }
}
