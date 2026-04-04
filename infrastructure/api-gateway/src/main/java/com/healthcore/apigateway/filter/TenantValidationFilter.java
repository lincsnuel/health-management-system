package com.healthcore.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TenantValidationFilter implements GlobalFilter, Ordered {

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        String resolvedTenant = exchange.getRequest()
                .getHeaders()
                .getFirst(GatewayHeaders.RESOLVED_TENANT_ID);

        String userTenant = exchange.getRequest()
                .getHeaders()
                .getFirst(GatewayHeaders.TENANT_ID);

        if (resolvedTenant == null) {
            log.warn("Request blocked: No resolved tenant for path '{}'", path);

            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }

        if (userTenant != null && !userTenant.equals(resolvedTenant)) {
            log.warn("Tenant mismatch: userTenant='{}', resolvedTenant='{}'", userTenant, resolvedTenant);

            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}