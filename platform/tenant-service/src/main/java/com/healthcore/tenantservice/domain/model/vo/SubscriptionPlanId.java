package com.healthcore.tenantservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record SubscriptionPlanId(UUID value) {

    public SubscriptionPlanId {
        Objects.requireNonNull(value, "SubscriptionPlanId must not be null");
    }

    /**
     * Factory for creating a new subscription plan ID (if needed)
     */
    public static SubscriptionPlanId newId() {
        return new SubscriptionPlanId(UUID.randomUUID());
    }
}