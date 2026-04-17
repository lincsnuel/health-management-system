package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.ProfessionalProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessionalProfileJpaRepository extends JpaRepository<ProfessionalProfileEntity, UUID> {

    Optional<ProfessionalProfileEntity> findByStaffId(UUID staffId);

    boolean existsByStaffId(UUID staffId);

    void deleteByStaffId(UUID staffId);
}