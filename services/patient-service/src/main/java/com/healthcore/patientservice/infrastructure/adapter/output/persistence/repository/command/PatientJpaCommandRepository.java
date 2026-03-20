package com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.command;

import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface PatientJpaCommandRepository
        extends JpaRepository<PatientEntity, UUID> {

    Optional<PatientEntity> findByPatientIdAndTenantId(
            UUID patientId,
            String tenantId
    );

    boolean existsByTenantIdAndEmail(
            String tenantId,
            String email
    );

    boolean existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );
}