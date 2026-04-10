package com.healthcore.patientservice.infrastructure.adapter.output.persistence;

import com.healthcore.patientservice.application.query.model.*;
import com.healthcore.patientservice.application.query.repository.PatientQueryRepository;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.query.PatientReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PatientJpaQueryAdapter implements PatientQueryRepository {

    private final PatientReadRepository repository;

    /* =========================
       SEARCH BY NAME (FIRST & LAST)
       ========================= */
    @Override
    public Page<PatientSummary> searchByName(
            String p1,
            String p2,
            Pageable pageable
    ) {
        // Repository should already return Page<PatientSummary> via JPQL constructor expression
        return repository.searchPatients(p1, p2, pageable);
    }

    /* =========================
       LIST PATIENTS FOR TENANT (SUMMARY / LIST VIEW)
       ========================= */
    @Override
    public Page<PatientListItem> findByTenant(Pageable pageable) {
        return repository.findPatientsForTenant(pageable);
    }

    /* =========================
       GET PATIENT DETAIL BY ID
       ========================= */
    @Override
    public Optional<PatientDetails> findPatientDetails(UUID patientId) {
        return repository.findPatientDetails(patientId);
    }

    /* =========================
       GET PATIENT CONTACT INFO BY EMAIL
       ========================= */
    @Override
    public Optional<PatientContactInfo> findContactInfoByEmail(String email) {
        return repository.findContactInfoByEmail(email);
    }

    @Override
    public List<EligiblePatientProjection> findEligiblePatients(int minAge, int maxAge) {
        return repository.findEligiblePatients(minAge, maxAge);
    }
}