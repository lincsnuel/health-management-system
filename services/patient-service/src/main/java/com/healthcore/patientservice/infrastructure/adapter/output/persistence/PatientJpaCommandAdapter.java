package com.healthcore.patientservice.infrastructure.adapter.output.persistence;

import com.healthcore.patientservice.domain.model.patient.Patient;
import com.healthcore.patientservice.domain.repository.PatientCommandRepository;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.command.PatientJpaCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PatientJpaCommandAdapter implements PatientCommandRepository {

    private final PatientJpaCommandRepository entityRepository;
    private final PatientEntityMapper patientMapper;

    /* =========================
       CREATE / UPDATE
       ========================= */
    @Override
    public Patient save(Patient patient) {
        // Map domain -> JPA entity
        PatientEntity entity = patientMapper.toEntity(patient);

        // Save to DB
        PatientEntity saved = entityRepository.save(entity);

        // Map back JPA -> domain
        return patientMapper.toDomain(saved);
    }

    /* =========================
       DATABASE QUERY OPERATIONS
       ========================= */
    public Optional<Patient> findById(UUID patientId) {
        return entityRepository.findById(patientId)
                .map(patientMapper::toDomain);
    }

    /* =========================
       DUPLICATE CHECKS
       ========================= */
    @Override
    public boolean existsByEmail(String email) {
        return entityRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByFirstNameAndLastNameAndDateOfBirth(
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    ) {
        return entityRepository.existsByFirstNameAndLastNameAndDateOfBirth(
                firstName, lastName, dateOfBirth
        );
    }
}