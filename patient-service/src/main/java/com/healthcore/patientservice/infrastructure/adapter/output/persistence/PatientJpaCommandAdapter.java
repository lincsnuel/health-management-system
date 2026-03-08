package com.healthcore.patientservice.infrastructure.adapter.output.persistence;

import com.healthcore.patientservice.domain.model.Patient;
import com.healthcore.patientservice.application.command.repository.PatientCommandRepository;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientEntity;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.mapper.PatientEntityMapper;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.PatientEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
@RequiredArgsConstructor
public class PatientJpaCommandAdapter implements PatientCommandRepository {

    private final PatientEntityRepository entityRepository;

    /* =========================
       CREATE / UPDATE
       ========================= */
    @Override
    public Patient save(Patient patient) {
        PatientEntity entity = PatientEntityMapper.toEntity(patient);
        PatientEntity saved = entityRepository.save(entity);
        return PatientEntityMapper.toDomain(saved);
    }

    /* =========================
       DUPLICATE CHECKS
       ========================= */

    @Override
    public boolean existsByTenantIdAndEmail(String tenantId, String email) {
        return entityRepository.existsByTenantIdAndEmail(tenantId, email);
    }

    @Override
    public boolean existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    ) {
        return entityRepository.existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
                tenantId, firstName, lastName, dateOfBirth
        );
    }
}