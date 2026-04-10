package com.healthcore.appointmentservice.application.command.port;

import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import com.healthcore.appointmentservice.domain.service.Availability;

import java.time.LocalDate;
import java.util.UUID;

public interface ScheduleServiceClient {

    Availability checkAvailability(
            UUID departmentId,
            LocalDate date,
            TimeSlot slot
    );
}