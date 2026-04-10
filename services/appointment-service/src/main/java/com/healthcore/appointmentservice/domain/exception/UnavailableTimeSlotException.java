package com.healthcore.appointmentservice.domain.exception;

public class UnavailableTimeSlotException extends DomainException {
    public UnavailableTimeSlotException(String msg) {
        super(msg);
    }
}
