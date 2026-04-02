package com.healthcore.workforceservice.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.application.query.model.StaffView;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity.StaffEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface StaffJpaRepository extends JpaRepository<StaffEntity, UUID> {

    Optional<StaffEntity> findByTenantIdAndEmail(String tenantId, String email);

    boolean existsByTenantIdAndEmail(String tenantId, String email);

    boolean existsByTenantIdAndFullName_FirstNameAndFullName_LastNameAndDateOfBirth(
            String tenantId,
            String firstName,
            String lastName,
            LocalDate dateOfBirth
    );

    @Query("""
        SELECT new com.healthcore.workforceservice.application.query.model.StaffView(
            s.staffId,
            CONCAT(s.fullName.firstName, ' ', s.fullName.lastName),
            s.email,
            s.departmentId,
            CAST(s.status AS string)
        )
        FROM StaffEntity s
        WHERE s.tenantId = :tenantId
    """)
    Page<StaffView> findByTenantId(@Param("tenantId") String tenantId, Pageable pageable);
}