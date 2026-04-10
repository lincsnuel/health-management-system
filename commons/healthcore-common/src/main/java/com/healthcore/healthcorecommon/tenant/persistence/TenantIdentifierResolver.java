package com.healthcore.healthcorecommon.tenant.persistence;

import com.healthcore.healthcorecommon.tenant.context.RequestContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

    @Override
    public String resolveCurrentTenantIdentifier() {
        // This pulls from your ThreadLocal set by the Filter
        return RequestContext.get()
                .map(RequestContext.Context::tenantId)
                .orElse("BOOTSTRAP_CONTEXT"); // When no context is set
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}