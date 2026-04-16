package com.healthcore.appointmentservice.domain.service;

import com.healthcore.appointmentservice.domain.exception.UnavailableTimeSlotException;
import com.healthcore.appointmentservice.domain.model.availability.Availability;
import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import com.healthcore.appointmentservice.domain.model.schedule.DepartmentScheduleProjection;

import java.time.LocalDate;

public class AppointmentDomainService {

    public Availability validateBooking(
            DepartmentScheduleProjection schedule,
            LocalDate date,
            TimeSlot slot,
            int booked
    ) {

        Availability availability =
                schedule.checkAvailability(date, slot, booked);

        if (!availability.isSlotAvailable()) {
            throw new UnavailableTimeSlotException(availability.getReason());
        }

        return availability;
    }
}