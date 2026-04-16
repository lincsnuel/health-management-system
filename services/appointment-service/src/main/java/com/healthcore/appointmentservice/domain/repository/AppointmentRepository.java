package com.healthcore.appointmentservice.domain.repository;

import com.healthcore.appointmentservice.domain.model.appointment.Appointment;
import com.healthcore.appointmentservice.domain.model.enums.TimeSlot;

import java.time.LocalDate;
import java.util.UUID;

public interface AppointmentRepository {

    int countConfirmedByDateAndSlot(
            UUID tenantId,
            UUID departmentId,
            LocalDate date,
            TimeSlot slot
    );

    void save(Appointment appointment);
}