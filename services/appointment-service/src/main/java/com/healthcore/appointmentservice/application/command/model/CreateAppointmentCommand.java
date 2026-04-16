package com.healthcore.appointmentservice.application.command.model;

import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;
import com.healthcore.appointmentservice.domain.model.vo.Attachment;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class CreateAppointmentCommand {

    public UUID tenantId;
    public UUID patientId; // from JWT
    public UUID departmentId;

    public LocalDate date;
    public TimeSlot timeSlot;

    public String referringHospital;
    public String notes;

    public String symptom;

    public List<Attachment> attachments;

    public String idempotencyKey;
}