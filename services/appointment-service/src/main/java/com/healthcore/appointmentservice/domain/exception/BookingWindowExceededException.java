package com.healthcore.appointmentservice.domain.exception;

public class BookingWindowExceededException extends DomainException {
    public BookingWindowExceededException() {
        super("Booking window exceeded");
    }
}