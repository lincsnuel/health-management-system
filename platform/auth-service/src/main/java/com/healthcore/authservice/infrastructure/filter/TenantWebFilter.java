package com.healthcore.authservice.infrastructure.filter;

import com.healthcore.authservice.common.context.TenantContext;
import org.jspecify.annotations.NullMarked;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Reactive Filter that extracts the X-Tenant-ID header and
 * puts it into the Reactor Context for downstream use.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this runs before security/business logic
public class TenantWebFilter implements WebFilter {

    private static final String TENANT_HEADER = "X-Resolved-Tenant-ID";

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 1. Extract the header from the reactive request
        String tenantId = exchange.getRequest().getHeaders().getFirst(TENANT_HEADER);

        // 2. Pass the request down the chain
        return chain.filter(exchange)
                // 3. Attach the tenantId to the "Context" of the entire stream
                .contextWrite(context -> {
                    if (tenantId != null) {
                        return context.put(TenantContext.TENANT_KEY, tenantId);
                    }
                    return context;
                });
    }
}