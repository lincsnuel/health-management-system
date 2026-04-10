package com.healthcore.appointmentservice.domain.service;

import com.healthcore.appointmentservice.domain.exception.UnavailableTimeSlotException;

public class AppointmentDomainService {

    public void validateBooking(Availability availability) {

        if (!availability.isSlotAvailable()) {
            throw new UnavailableTimeSlotException("");
        }
    }

    public void validateReschedule(Availability availability) {

        if (!availability.isSlotAvailable()) {
            throw new UnavailableTimeSlotException("");
        }
    }
}