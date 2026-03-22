package com.healthcore.healthcorecommon.tenant.context;

import org.jspecify.annotations.NullMarked;
import org.springframework.core.task.TaskDecorator;

/**
 * When it is actually needed
 * Only if you have asynchronous operations that touch repositories or services relying on TenantContext.
 * Examples:
 * Sending welcome email to a patient after registration (async) and needing tenant info.
 * Background processing of records for a specific tenant.
 * Scheduled tasks per tenant.
 */

public class TenantAwareTaskDecorator implements TaskDecorator {

    @Override
    @NullMarked
    public Runnable decorate(Runnable runnable) {
        String tenantId = TenantContext.getTenantId();

        return () -> {
            try {
                TenantContext.setTenantId(tenantId);
                runnable.run();
            } finally {
                TenantContext.clear();
            }
        };
    }
}