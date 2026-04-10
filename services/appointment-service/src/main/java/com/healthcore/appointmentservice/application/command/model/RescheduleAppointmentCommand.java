package com.healthcore.appointmentservice.application.command.model;

import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;

import java.time.LocalDate;
import java.util.UUID;

public class RescheduleAppointmentCommand {

    public UUID appointmentId;
    public UUID tenantId;

    public LocalDate newDate;
    public TimeSlot newSlot;
}