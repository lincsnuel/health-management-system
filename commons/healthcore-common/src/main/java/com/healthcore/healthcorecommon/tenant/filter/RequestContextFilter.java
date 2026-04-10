package com.healthcore.healthcorecommon.tenant.filter;

import com.healthcore.healthcorecommon.exception.MissingHeaderException;
import com.healthcore.healthcorecommon.tenant.context.HeaderConstants;
import com.healthcore.healthcorecommon.tenant.context.RequestContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class RequestContextFilter extends OncePerRequestFilter {

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String tenantId = request.getHeader(HeaderConstants.TENANT_ID);
        String userId = request.getHeader(HeaderConstants.USER_ID);
        String userType = request.getHeader(HeaderConstants.USER_TYPE);
        String rolesHeader = request.getHeader(HeaderConstants.ROLES);

        try {
            validate(tenantId, userId);

            Set<String> roles = rolesHeader == null || rolesHeader.isBlank()
                    ? Set.of()
                    : Arrays.stream(rolesHeader.split(";"))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            RequestContext.set(new RequestContext.Context(
                    tenantId,
                    userId,
                    userType,
                    roles
            ));

            // Log successful setup at DEBUG level to avoid log spam in production
            log.debug("Successfully set RequestContext for Tenant: {}, User: {}, Roles: {}",
                    tenantId, userId, roles);

            filterChain.doFilter(request, response);

        } catch (MissingHeaderException e) {
            log.error("Request failed validation: {} (Tenant: {}, User: {})",
                    e.getMessage(), tenantId, userId);
            throw e;
        } finally {
            RequestContext.clear();
            log.trace("Cleared RequestContext for request");
        }
    }

    private void validate(String tenantId, String userId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new MissingHeaderException("Missing Tenant ID header");
        }
        if (userId == null || userId.isBlank()) {
            throw new MissingHeaderException("Missing User ID header");
        }
    }
}