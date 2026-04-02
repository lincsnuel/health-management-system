package com.healthcore.apigateway.filter;

import org.jspecify.annotations.NullMarked;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtToHeaderFilter implements GlobalFilter, Ordered {

    @Override
    @NullMarked
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1. Get the current request path to check for exclusions
        String path = exchange.getRequest().getURI().getPath();

        // 2. Skip authentication for auth-related endpoints (Login/Register)
        // to avoid looking for tokens that don't exist yet
        if (path.startsWith("/auth")) {
            return chain.filter(exchange);
        }

        // 3. Extract the Security Principal (the authenticated user) from the reactive context
        return exchange.getPrincipal()
                // Cast it specifically to a JWT token type
                .cast(JwtAuthenticationToken.class)
                .flatMap(jwtAuth -> {

                    // 4. Extract claims (data fields) from the decoded JWT
                    Jwt jwt = jwtAuth.getToken();

                    // 5. Keep the Keycloak Internal ID for sync purposes
                    String keycloakUuid = jwt.getSubject();

                    // 6. Extract CUSTOM business ID set in the KeycloakUserMapper in auth-service
                    // This is the "userId" attribute saved in Keycloak
                    String businessId = jwt.getClaimAsString("userId");
                    String tenantId = jwt.getClaimAsString("tenantId");
                    String userType = jwt.getClaimAsString("userType");
                    List<String> roles = jwt.getClaimAsStringList("roles");

                    // 7. Mutate (modify) the incoming request to add custom headers
                    // We use "X-" prefix as a convention for custom metadata headers
                    ServerHttpRequest.Builder builder = exchange.getRequest().mutate()
                            .header("X-Keycloak-UUID", keycloakUuid); // Optional: for debugging

                    // 8. Only add headers if the specific claim exists in the token
                    // IMPORTANT: This is what your microservices likely care about
                    if (businessId != null) {
                        builder.header("X-User-ID", businessId);
                    }
                    if (tenantId != null) {
                        builder.header("X-Tenant-ID", tenantId);
                    }
                    if (userType != null) {
                        builder.header("X-User-Type", userType);
                    }

                    // 9. Convert the list of roles into a comma-separated string
                    if (roles != null && !roles.isEmpty()) {
                        builder.header("X-Roles", String.join(",", roles));
                    }

                    // 10. Forward the "mutated" request (with new headers) to the next filter/service
                    return chain.filter(exchange.mutate().request(builder.build()).build());
                })
                // 11. If no principal/JWT is found (e.g., anonymous request), just pass it through
                .switchIfEmpty(chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        // Defines the priority. 0 means this runs very early in the filter chain
        return 0;
    }
}