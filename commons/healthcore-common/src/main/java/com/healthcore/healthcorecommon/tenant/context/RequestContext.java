package com.healthcore.healthcorecommon.tenant.context;

import com.healthcore.healthcorecommon.exception.NullContextException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * Manages request-scoped context data for both gRPC and Web/REST flows.
 */
public final class RequestContext {

    public static final io.grpc.Context.Key<Context> GRPC_CONTEXT_KEY = io.grpc.Context.key("request-context");
    private static final ThreadLocal<Context> CONTEXT = new ThreadLocal<>();

    private RequestContext() {}

    public static void set(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        CONTEXT.set(context);
    }

    /**
     * Attempts to retrieve the current Context.
     * Returns an Optional to allow callers to handle absence gracefully.
     */
    public static Optional<Context> get() {
        // 1. Try to get from gRPC Context (Priority)
        Context grpcCtx = GRPC_CONTEXT_KEY.get();
        if (grpcCtx != null) {
            return Optional.of(grpcCtx);
        }

        // 2. Fallback to ThreadLocal (Web/REST)
        return Optional.ofNullable(CONTEXT.get());
    }

    /**
     * Helper to get context or throw an exception when context is strictly required.
     */
    public static Context getRequired() {
        return get().orElseThrow(() -> new NullContextException("RequestContext not initialized"));
    }

    public static void clear() {
        CONTEXT.remove();
    }

    /* =========================================================
       TENANT & AUTH METHODS
       ========================================================= */

    public static String getTenantId() {
        return getRequired().tenantId();
    }

    public static boolean isAuthenticated() {
        return get().map(ctx -> ctx.userId() != null && !ctx.userId().isBlank())
                .orElse(false);
    }

    public static String getUserId() {
        return get().map(Context::userId)
                .filter(id -> !id.isBlank())
                .orElseThrow(() -> new IllegalStateException("User is not authenticated"));
    }

    public static String getUserType() {
        return get().map(Context::userType)
                .filter(type -> !type.isBlank())
                .orElseThrow(() -> new IllegalStateException("User type not available"));
    }

    public static Set<String> getRoles() {
        return get().map(Context::roles).orElse(Collections.emptySet());
    }

    /* =========================================================
       CONTEXT RECORD
       ========================================================= */
    public record Context(
            String tenantId,
            String userId,
            String userType,
            Set<String> roles
    ) {
        public Context {
            roles = (roles == null) ? Collections.emptySet() : Set.copyOf(roles);
            if (tenantId == null || tenantId.isBlank()) {
                throw new IllegalArgumentException("tenantId is required");
            }
        }
    }
}