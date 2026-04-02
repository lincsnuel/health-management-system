package com.healthcore.workforceservice.infrastructure.adapter.output.persistence;

import com.healthcore.workforceservice.application.port.output.DepartmentValidationPort;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.repository.DepartmentProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DepartmentValidationAdapter implements DepartmentValidationPort {

    private final DepartmentProjectionRepository repository;

    @Override
    public boolean exists(String tenantId, String departmentId) {
        return repository.existsByTenantIdAndDepartmentId(tenantId, departmentId);
    }
}