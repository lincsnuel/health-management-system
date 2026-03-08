package com.healthcore.patientservice.domain.exception;

public class AddressNotFoundException extends DomainException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
