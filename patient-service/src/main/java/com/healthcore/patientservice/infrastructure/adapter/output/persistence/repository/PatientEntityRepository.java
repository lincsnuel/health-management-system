package com.healthcore.patientservice.infrastructure.adapter.output.persistence.repository;

import com.healthcore.patientservice.application.query.model.*;
import com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity.PatientEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientEntityRepository extends JpaRepository<PatientEntity, UUID> {

    /* =========================================================
       SEARCH PATIENTS BY NAME (SUMMARY VIEW)
       ========================================================= */
    @Query("""
        SELECT new com.healthcore.patientservice.application.query.model.PatientSummary(
            p.patientId,
            p.hospitalPatientNumber,
            CONCAT(p.firstName, ' ', p.lastName),
            p.gender,
            p.dateOfBirth,
            p.contactNumber,
            p.status
        )
        FROM PatientEntity p
        WHERE p.tenantId = :tenantId
          AND (
            (LOWER(p.firstName) LIKE :p1 AND LOWER(p.lastName) LIKE :p2) OR
            (LOWER(p.firstName) LIKE :p2 AND LOWER(p.lastName) LIKE :p1) OR
            LOWER(p.firstName) LIKE :p1 OR
            LOWER(p.lastName) LIKE :p1
          )
    """)
    Page<PatientSummary> searchPatients(
            @Param("p1") String p1,
            @Param("p2") String p2,
            @Param("tenantId") String tenantId,
            Pageable pageable
    );

    /* =========================================================
       FIND ALL PATIENTS FOR TENANT (LIST VIEW)
       ========================================================= */
    @Query("""
        SELECT new com.healthcore.patientservice.application.query.model.PatientListItem(
            p.patientId,
            p.hospitalPatientNumber,
            p.firstName,
            p.lastName,
            p.gender,
            p.dateOfBirth,
            p.contactNumber,
            p.status
        )
        FROM PatientEntity p
        WHERE p.tenantId = :tenantId
    """)
    Page<PatientListItem> findAllByTenant(@Param("tenantId") String tenantId, Pageable pageable);

    /* =========================================================
       FIND PATIENT DETAIL BY ID (INCLUDES NESTED ADDRESSES & INSURANCE)
       ========================================================= */
    @EntityGraph(attributePaths = {"addresses", "insurances"})
    @Query("SELECT p FROM PatientEntity p WHERE p.patientId = :patientId AND p.tenantId = :tenantId")
    Optional<PatientEntity> findPatientById(@Param("patientId") UUID patientId, @Param("tenantId") String tenantId);

    /* =========================================================
       FIND PATIENT CONTACT INFO BY EMAIL
       ========================================================= */
    @Query("""
        SELECT new com.healthcore.patientservice.application.query.model.PatientContactInfo(
            p.patientId,
            CONCAT(p.firstName, ' ', p.lastName),
            p.email,
            p.contactNumber
        )
        FROM PatientEntity p
        WHERE p.email = :email AND p.tenantId = :tenantId
    """)
    Optional<PatientContactInfo> findContactInfoByEmail(
            @Param("email") String email,
            @Param("tenantId") String tenantId
    );

    /* =========================================================
       HARD RULE: CHECK IF PATIENT EXISTS BY EMAIL (TENANT-SCOPED)
       ========================================================= */
    boolean existsByTenantIdAndEmail(String tenantId, String email);

    /* =========================================================
       SOFT RULE: CHECK IF PATIENT EXISTS BY NAME + DOB (TENANT-SCOPED)
       ========================================================= */
    boolean existsByTenantIdAndFirstNameAndLastNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );

    /* =========================================================
       OPTIONAL / UTILITY METHODS
       ========================================================= */
    Optional<PatientEntity> findByPatientIdAndTenantId(UUID patientId, String tenantId);

    Optional<PatientEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}