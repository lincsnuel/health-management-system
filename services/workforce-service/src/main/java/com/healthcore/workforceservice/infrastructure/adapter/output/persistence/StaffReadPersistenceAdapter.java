package com.healthcore.workforceservice.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.application.query.model.StaffView;
import com.healthcore.workforceservice.application.query.repository.StaffReadRepository;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.repository.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StaffReadPersistenceAdapter implements StaffReadRepository {
    private final StaffJpaRepository repository;

    @Override
    public Page<StaffView> findByTenant(String tenantId, Pageable pageable) {
        return repository.findByTenantId(tenantId, pageable);
    }
}
