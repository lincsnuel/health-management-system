package com.healthcore.authservice.common.context;

import reactor.core.publisher.Mono;

/**
 * Reactive replacement for ThreadLocal TenantContext.
 * Uses Reactor Context to propagate the Tenant ID through the reactive pipeline.
 */
public class TenantContext {

    public static final String TENANT_KEY = "tenantId";

    /**
     * Extracts the Tenant ID from the Reactive Context.
     * Use this in Service layer.
     * * Example usage:
     * TenantContext.getTenantId().flatMap(id -> ...);
     */
    public static Mono<String> getTenantId() {
        return Mono.deferContextual(ctx -> {
            if (ctx.hasKey(TENANT_KEY)) {
                return Mono.just(ctx.get(TENANT_KEY));
            }
            return Mono.empty();
        });
    }

    /*
     * Helper to clear/check context is not needed manually in Reactive
     * as the context is immutable and scoped to the specific request signal.
     */
}