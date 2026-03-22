package com.healthcore.tenantservice.infrastructure.adapter.output.persistence;

import com.healthcore.tenantservice.domain.model.tenant.Tenant;
import com.healthcore.tenantservice.domain.repository.TenantRepository;
import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.entity.TenantEntity;
import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.mapper.TenantMapper;
import com.healthcore.tenantservice.infrastructure.adapter.output.persistence.repository.TenantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TenantJpaAdapter implements TenantRepository {

    private final TenantJpaRepository jpaRepository;

    @Override
    public Tenant save(Tenant tenant) {

        TenantEntity entity = TenantMapper.toEntity(tenant);

        jpaRepository.save(entity);

        return tenant;
    }

    @Override
    public Optional<Tenant> findById(String id) {
        return jpaRepository.findById(UUID.fromString(id))
                .map(TenantMapper::toDomain);
    }
}