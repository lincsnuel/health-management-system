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
 * Reactive Filter that extracts Tenant and User metadata and
 * propagates it via Reactor Context.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantWebFilter implements WebFilter {

    private static final String TENANT_HEADER = "X-Resolved-Tenant-ID";
    private static final String USER_ID_HEADER = "X-User-ID"; // Standard header from Gateway/Security

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 1. Extract headers
        String tenantId = exchange.getRequest().getHeaders().getFirst(TENANT_HEADER);
        String userId = exchange.getRequest().getHeaders().getFirst(USER_ID_HEADER);

        // 2. Pass down the chain and write to context
        return chain.filter(exchange)
                .contextWrite(context -> {
                    var ctx = context;
                    if (tenantId != null) {
                        ctx = ctx.put(TenantContext.TENANT_KEY, tenantId);
                    }
                    if (userId != null) {
                        ctx = ctx.put(TenantContext.USER_ID_KEY, userId);
                    }
                    return ctx;
                });
    }
}