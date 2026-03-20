package com.healthcore.patientservice.domain.exception;

public class InvalidPatientStatusException extends DomainException {
    public InvalidPatientStatusException(String message) {
        super(message);
    }
}
