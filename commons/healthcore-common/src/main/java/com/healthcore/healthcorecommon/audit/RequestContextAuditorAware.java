package com.healthcore.healthcorecommon.audit;

import com.healthcore.healthcorecommon.tenant.context.RequestContext;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class RequestContextAuditorAware implements AuditorAware<String> {

    @Override
    @NullMarked
    public Optional<String> getCurrentAuditor() {

        try {
            // Only return if authenticated
            if (RequestContext.isAuthenticated()) {
                return Optional.of(RequestContext.getUserId());
            }
        } catch (Exception ignored) {}

        // Registration/system flows → no user yet
        // Use Tenant-Scoped System Identity for when userId is null
        return Optional.of("SYSTEM@" + RequestContext.getTenantId());
    }
}