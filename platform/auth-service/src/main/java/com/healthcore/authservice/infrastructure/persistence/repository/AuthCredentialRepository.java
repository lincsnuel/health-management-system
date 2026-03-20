package com.healthcore.authservice.infrastructure.persistence.repository;

import com.healthcore.authservice.infrastructure.persistence.entity.AuthCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCredentialRepository extends JpaRepository<AuthCredentialEntity, String> {

    Optional<AuthCredentialEntity> findByStaffIdAndTenantId(String staffId, String tenantId);
}