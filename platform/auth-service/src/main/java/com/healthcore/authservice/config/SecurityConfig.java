package com.healthcore.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; // Changed
import org.springframework.security.config.web.server.ServerHttpSecurity; // Changed
import org.springframework.security.web.server.SecurityWebFilterChain; // Changed

@Configuration
@EnableWebFluxSecurity // Changed from @EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                // Disable CSRF for the API endpoints
                // In WebFlux, CSRF is disabled like this
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // // Explicitly allow the OTP paths "/auth/**" and "/error" permitted
                        // /error allows Spring's internal error handling to avoid the 401 flip
                        .pathMatchers("/auth/**", "/error").permitAll()
                        .anyExchange().authenticated()
                )
                .build();
    }
}