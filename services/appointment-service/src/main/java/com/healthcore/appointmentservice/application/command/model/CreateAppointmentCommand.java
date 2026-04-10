package com.healthcore.appointmentservice.application.command.model;

import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;

import java.time.LocalDate;
import java.util.UUID;

public class CreateAppointmentCommand {

    public UUID tenantId;
    public UUID patientId; // from JWT
    public UUID departmentId;

    public LocalDate date;
    public TimeSlot timeSlot;

    public String symptom;
    public String idempotencyKey;
}