package com.healthcore.tenantservice.infrastructure.adapter.output.persistence.repository;

import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantJpaRepository extends JpaRepository<TenantEntity, UUID> {
}
