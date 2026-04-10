package com.healthcore.appointmentservice.domain.repository;

import com.healthcore.appointmentservice.domain.model.appointment.Appointment;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentCommandRepository {

    boolean existsByTenantIdAndEmail(String tenantId, String email);

    boolean existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(UUID appointmentId, UUID tenantId);
}