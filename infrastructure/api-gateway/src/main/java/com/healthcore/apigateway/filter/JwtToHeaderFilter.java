package com.healthcore.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class JwtToHeaderFilter implements GlobalFilter, Ordered {

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        return exchange.getPrincipal()
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuth -> {

                    Jwt jwt = jwtAuth.getToken();

                    String keycloakUuid = jwt.getSubject();
                    String businessId = jwt.getClaimAsString("userId");
                    String tenantId = jwt.getClaimAsString("tenantId");
                    String userType = jwt.getClaimAsString("userType");
                    List<String> roles = jwt.getClaimAsStringList("roles");

                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate()
                            .header(GatewayHeaders.KEYCLOAK_UUID, keycloakUuid);

                    if (businessId != null) {
                        builder.header(GatewayHeaders.USER_ID, businessId);
                    }
                    if (tenantId != null) {
                        builder.header(GatewayHeaders.TENANT_ID, tenantId);
                    }
                    if (userType != null) {
                        builder.header(GatewayHeaders.USER_TYPE, userType);
                    }
                    if (roles != null && !roles.isEmpty()) {
                        builder.header(GatewayHeaders.ROLES, String.join(",", roles));
                    }

                    log.debug("JWT headers injected for user '{}'", businessId);

                    return chain.filter(exchange.mutate().request(builder.build()).build());
                })
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}