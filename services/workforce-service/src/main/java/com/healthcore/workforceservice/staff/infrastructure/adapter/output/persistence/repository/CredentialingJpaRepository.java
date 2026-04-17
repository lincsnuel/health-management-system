package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.CredentialingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CredentialingJpaRepository extends JpaRepository<CredentialingEntity, UUID> {

    Optional<CredentialingEntity> findByStaffId(UUID staffId);

    boolean existsByStaffId(UUID staffId);

    void deleteByStaffId(UUID staffId);
}