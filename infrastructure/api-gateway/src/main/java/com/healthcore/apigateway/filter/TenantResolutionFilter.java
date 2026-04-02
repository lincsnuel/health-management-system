package com.healthcore.apigateway.filter;

import com.healthcore.apigateway.grpc.TenantGrpcClient;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TenantResolutionFilter implements GlobalFilter, Ordered {

    private final TenantGrpcClient tenantGrpcClient;

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange rawExchange, GatewayFilterChain chain) {
        /*
            Before injecting headers:
            Remove incoming headers
         */

        System.out.println("Before mutation: " + rawExchange.getRequest().getHeaders());

        ServerWebExchange mutatedExchange = rawExchange.mutate()
                .request(rawExchange.getRequest().mutate()
                        .headers(headers -> {
                            headers.remove("X-User-ID");
                            headers.remove("X-Tenant-ID");
                            headers.remove("X-User-Type");
                            headers.remove("X-Roles");
                        })
                        .build()) // Builds the new Request
                .build();     // Builds the new Exchange

        System.out.println("After mutation: " + mutatedExchange.getRequest().getHeaders());

        String host = mutatedExchange.getRequest().getHeaders().getFirst("Host");

        if (host == null) {
            return chain.filter(mutatedExchange);
        }

        String subdomain = extractSubdomain(host);

        System.out.println("Inside TenantResolutionFilter filter before return tenantGrpcClient");

        // 1. Call the method directly (it already returns a Mono)
        return tenantGrpcClient.resolveTenant(subdomain)
                // 2. flatMap now receives the ACTUAL String ID from the gRPC success
                .flatMap(tenantId -> {

                    System.out.println("Type of tenantId: " + tenantId.getClass().getName());

                    // Just mutate the exchange ONCE here with the final header
                    ServerWebExchange finalExchange = mutatedExchange.mutate()
                            .request(r -> r.header("X-Tenant-ID", tenantId))
                            .build();

                    return chain.filter(finalExchange);
                })
                .onErrorResume(e -> {
                    // Log the error
                    System.err.println("Tenant Resolution failed for host. Error: " + e.getMessage());

                    mutatedExchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);

                    return mutatedExchange.getResponse().setComplete();
                });
    }

    private String extractSubdomain(String host) {
        return host.split("\\.")[0];
    }

    @Override
    public int getOrder() {
        return -1; // VERY IMPORTANT (runs first)
    }
}