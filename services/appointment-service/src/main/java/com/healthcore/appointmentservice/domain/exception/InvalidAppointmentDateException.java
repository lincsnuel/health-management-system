package com.healthcore.appointmentservice.domain.exception;

public class InvalidAppointmentDateException extends DomainException {
    public InvalidAppointmentDateException(String msg) {
        super(msg);
    }
}