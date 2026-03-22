package com.healthcore.tenantservice.application.command;

import com.healthcore.tenantservice.domain.model.vo.SubscriptionPlanId;
import com.healthcore.tenantservice.domain.model.vo.TenantKey;

public record CreateTenantCommand(
        TenantKey tenantKey,
        String name,
        SubscriptionPlanId subscriptionPlanId
) {
}