package com.healthcore.healthcorecommon.tenant.context;

import com.healthcore.healthcorecommon.exception.NullTenantIdException;

public final class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new NullTenantIdException("Tenant ID cannot be null or blank");
        }
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        String tenantId = CURRENT_TENANT.get();

        if (tenantId == null) {
            throw new NullTenantIdException("Tenant ID is not set in context");
        }

        return tenantId;
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}