package com.healthcore.patientservice.application.query.repository;

import com.healthcore.patientservice.application.query.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PatientQueryRepository {

    /* =========================
       SEARCH / LIST OPERATIONS
       ========================= */

    /** Search patients by name for UI summary view */
    Page<PatientSummary> searchByName(String p1, String p2, String tenantId, Pageable pageable);

    /** List all patients for a tenant with pagination */
    Page<PatientListItem> findByIdAndTenantId(String tenantId, Pageable pageable);

    /** Get a patient detail view for a given patient */
    Optional<PatientDetails> findPatientById(UUID patientId, String tenantId);

    /** Get basic contact info for a patient by email (used in validations or messaging) */
    Optional<PatientContactInfo> findContactInfoByEmail(String email, String tenantId);

    /** Check if a patient exists for a given tenant (for validations) */
    boolean existsByEmail(String email, String tenantId);
}