package com.healthcore.authservice.common.context;

import reactor.core.publisher.Mono;

public class TenantContext {

    public static final String TENANT_KEY = "tenantId";
    public static final String USER_ID_KEY = "userId"; // Added for session management

    public static Mono<String> getTenantId() {
        return Mono.deferContextual(ctx -> ctx.hasKey(TENANT_KEY)
                ? Mono.just(ctx.get(TENANT_KEY)) : Mono.empty());
    }

    public static Mono<String> getUserId() {
        return Mono.deferContextual(ctx -> ctx.hasKey(USER_ID_KEY)
                ? Mono.just(ctx.get(USER_ID_KEY)) : Mono.empty());
    }
}