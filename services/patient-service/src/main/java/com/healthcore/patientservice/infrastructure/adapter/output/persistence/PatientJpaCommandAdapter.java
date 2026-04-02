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
    private final AddressEntityMapper addressMapper;
    private final InsuranceEntityMapper insuranceMapper;
    private final ResponsiblePartyEntityMapper responsiblePartyMapper;
    private final DocumentEntityMapper documentMapper;

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
    public Optional<Patient> findById(UUID patientId, String tenantId) {
        return entityRepository.findByPatientIdAndTenantId(patientId, tenantId)
                .map(patientMapper::toDomain);
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