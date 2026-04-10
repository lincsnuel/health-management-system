package com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.command;

import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface PatientJpaCommandRepository
        extends JpaRepository<PatientEntity, UUID> {

    boolean existsByEmail(String email);

    boolean existsByFirstNameAndLastNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );
}