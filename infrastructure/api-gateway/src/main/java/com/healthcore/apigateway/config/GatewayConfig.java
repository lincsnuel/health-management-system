package com.healthcore.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // ==========================
                // Patient Service Route
                // ==========================
                .route("patient-service", r -> r
                        .path("/api/v1/patients/**")
                        .uri("http://localhost:8083")
                )

                // ==========================
                // Tenant Service Route
                // ==========================
                .route("tenant-service", r -> r
                                .path("/api/v1/tenants/**")
                                .uri("http://localhost:8087")
                )

                // ==========================
                // Workforce Service Route
                // ==========================
                .route("workforce-service", r -> r
                        .path("/api/v1/workforce/**")
                        .uri("http://localhost:8084")
                )

                // ==========================
                // Auth Service Route
                // ==========================
                .route("auth-service", r -> r
                        .path("/auth/**")
                        .uri("http://localhost:8088")
                )
                .build();
    }
}