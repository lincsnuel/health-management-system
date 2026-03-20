package com.healthcore.patientservice.domain.exception;

public class InactivePatientOperationException extends DomainException {
    public InactivePatientOperationException(String message) {
        super(message);
    }
}
