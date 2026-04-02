package com.healthcore.tenantservice.infrastructure.adapter.input.rest.mapper;

import com.healthcore.tenantservice.domain.model.tenant.*;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.BranchResponse;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.DepartmentResponse;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.TenantResponse;

import java.util.stream.Collectors;

public class TenantResponseMapper {

    public static TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId().value().toString(),
                tenant.getTenantKey().value().toString(),
                tenant.getName(),
                tenant.getSubdomain().value(),
                tenant.getStatus(),
                tenant.isSetupCompleted(),
                tenant.getSubscriptionPlanId().value(),
                tenant.getBranches().stream()
                        .map(TenantResponseMapper::toBranch)
                        .collect(Collectors.toList()),
                tenant.getDepartments().stream()
                        .map(TenantResponseMapper::toDepartment)
                        .collect(Collectors.toList())
        );
    }

    private static BranchResponse toBranch(FacilityBranch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getBranchName(),
                branch.isMain(),
                branch.isActive()
        );
    }

    private static DepartmentResponse toDepartment(Department dept) {
        return new DepartmentResponse(
                dept.getId(),
                dept.getName(),
                dept.isActive()
        );
    }
}