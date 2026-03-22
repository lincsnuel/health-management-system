package com.healthcore.tenantservice.infrastructure.adapter.input.rest.controller;

import com.healthcore.tenantservice.application.command.CreateTenantCommand;
import com.healthcore.tenantservice.application.usecase.CreateTenantUseCase;
import com.healthcore.tenantservice.application.usecase.QueryTenantUseCase;
import com.healthcore.tenantservice.domain.model.vo.SubscriptionPlanId;
import com.healthcore.tenantservice.domain.model.vo.TenantKey;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.request.CreateTenantRequest;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.TenantResponse;
import com.healthcore.tenantservice.infrastructure.adapter.input.rest.dto.response.TenantResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final CreateTenantUseCase createService;
    private final QueryTenantUseCase  queryService;

    @PostMapping
    public TenantResult createTenant(@RequestBody CreateTenantRequest request) {

        CreateTenantCommand command = new CreateTenantCommand(
                new TenantKey(request.tenantKey()),
                request.name(),
                new SubscriptionPlanId(UUID.fromString(request.subscriptionPlanId()))
        );

        var tenant = createService.createTenant(command);

        return new TenantResult(
                tenant.getId().value().toString(),
                tenant.getTenantKey().value(),
                tenant.getName(),
                tenant.getStatus().name()
        );
    }

    @GetMapping("/{tenantId}")
    public TenantResponse getTenant(@PathVariable String tenantId) {
        return queryService.findByTenantId(tenantId);
    }

}