package com.healthcore.tenantservice.application.usecase;

import com.healthcore.tenantservice.application.command.CreateTenantCommand;
import com.healthcore.tenantservice.domain.model.tenant.Tenant;

public interface CreateTenantUseCase {

    Tenant createTenant(CreateTenantCommand command);
}
