package com.healthcore.patientservice.application.command.repository;

import com.healthcore.patientservice.domain.model.patient.Patient;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PatientCommandRepository {

    boolean existsByTenantIdAndEmail(String tenantId, String email);

    boolean existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );

    Patient save(Patient patient);

    Optional<Patient> findById(UUID patientId, String tenantId);
}