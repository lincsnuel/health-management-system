package com.healthcore.patientservice.domain.exception;

public class AddressLimitExceededException extends DomainException {
    public AddressLimitExceededException(String message) {
        super(message);
    }
}
