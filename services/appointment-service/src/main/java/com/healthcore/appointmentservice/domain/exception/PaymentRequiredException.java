package com.healthcore.appointmentservice.domain.exception;

public class PaymentRequiredException extends DomainException {
    public PaymentRequiredException(String message) {
        super(message);
    }
}
