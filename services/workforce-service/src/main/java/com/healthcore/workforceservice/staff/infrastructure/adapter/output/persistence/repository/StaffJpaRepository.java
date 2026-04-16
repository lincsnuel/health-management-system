package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.staff.application.query.model.StaffView;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.StaffEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface StaffJpaRepository extends JpaRepository<StaffEntity, UUID> {

    Optional<StaffEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByFullName_FirstNameAndFullName_LastNameAndDateOfBirth(
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
    """)
    Page<StaffView> findByTenantId(Pageable pageable);
}