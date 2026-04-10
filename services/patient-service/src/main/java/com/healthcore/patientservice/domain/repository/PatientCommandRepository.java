package com.healthcore.patientservice.domain.repository;

import com.healthcore.patientservice.domain.model.patient.Patient;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PatientCommandRepository {

    boolean existsByEmail(String email);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );

    Patient save(Patient patient);

    Optional<Patient> findById(UUID patientId);
}