package com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository.query;

import com.healthcore.patientservice.application.query.model.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientReadRepository {

    Page<PatientSummary> searchPatients(
            String p1,
            String p2,
            String tenantId,
            Pageable pageable
    );

    Page<PatientListItem> findPatientsForTenant(
            String tenantId,
            Pageable pageable
    );

    Optional<PatientDetails> findPatientDetails(
            UUID patientId,
            String tenantId
    );

    Optional<PatientContactInfo> findContactInfoByEmail(
            String email,
            String tenantId
    );

    List<EligiblePatientProjection> findEligiblePatients(int minAge, int maxAge);

    Optional<PatientContact> findByTenantIdAndPhoneNumber(String tenantId, String phoneNumber);
}