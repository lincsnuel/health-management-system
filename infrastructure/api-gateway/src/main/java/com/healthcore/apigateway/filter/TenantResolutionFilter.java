package com.healthcore.apigateway.filter;

import com.healthcore.apigateway.grpc.TenantGrpcClient;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TenantResolutionFilter implements GlobalFilter, Ordered {

    private final TenantGrpcClient tenantGrpcClient;

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String host = exchange.getRequest().getHeaders().getFirst("Host");

        if (host == null) {
            log.debug("No host header present. Skipping tenant resolution.");
            return chain.filter(exchange);
        }

        String subdomain = extractSubdomain(host);

        return tenantGrpcClient.resolveTenant(subdomain)
                .flatMap(tenantId -> {

                    log.debug("Resolved tenant '{}' for subdomain '{}'", tenantId, subdomain);

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(r -> r.headers(headers -> {
                                headers.remove(GatewayHeaders.USER_ID);
                                headers.remove(GatewayHeaders.TENANT_ID);
                                headers.remove(GatewayHeaders.USER_TYPE);
                                headers.remove(GatewayHeaders.ROLES);

                                headers.add(GatewayHeaders.RESOLVED_TENANT_ID, tenantId);
                            }))
                            .build();

                    return chain.filter(mutatedExchange);
                })
                .onErrorResume(e -> {
                    log.error("Tenant resolution failed for subdomain '{}': {}", subdomain, e.getMessage(), e);

                    exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
                    return exchange.getResponse().setComplete();
                });
    }

    private String extractSubdomain(String host) {
        return host.split("\\.")[0];
    }

    @Override
    public int getOrder() {
        return -10; // Runs first
    }
}