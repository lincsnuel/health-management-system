package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.staff.application.query.model.StaffView;
import com.healthcore.workforceservice.staff.application.query.repository.StaffReadRepository;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.StaffJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StaffReadPersistenceAdapter implements StaffReadRepository {
    private final StaffJpaRepository repository;

    @Override
    public Page<StaffView> findByTenantId(Pageable pageable) {
        return repository.findByTenantId(pageable);
    }
}
