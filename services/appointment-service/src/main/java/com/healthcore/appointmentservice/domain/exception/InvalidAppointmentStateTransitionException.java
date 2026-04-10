package com.healthcore.appointmentservice.domain.exception;

public class InvalidAppointmentStateTransitionException extends DomainException {
    public InvalidAppointmentStateTransitionException(String msg) {
        super(msg);
    }
}