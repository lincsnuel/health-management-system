package com.healthcore.patientservice.domain.exception;

public class CannotRemovePrimaryAddressException extends DomainException {
    public CannotRemovePrimaryAddressException(String message) {
        super(message);
    }
}
