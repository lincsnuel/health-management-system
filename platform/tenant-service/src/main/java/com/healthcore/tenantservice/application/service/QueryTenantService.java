package com.healthcore.tenantservice.application.service;

import com.healthcore.tenantservice.application.exception.TenantNotFoundException;
import com.healthcore.tenantservice.application.usecase.QueryTenantUseCase;
import com.healthcore.tenantservice.domain.model.tenant.Tenant;
import com.healthcore.tenantservice.domain.repository.TenantRepository;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.TenantResponse;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.mapper.TenantResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryTenantService implements QueryTenantUseCase {
    private final TenantRepository repository;

    @Override
    public TenantResponse findByTenantId(String tenantId) {
        return repository.findById(tenantId)
                .map(TenantResponseMapper::toResponse)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found"));
    }
}
