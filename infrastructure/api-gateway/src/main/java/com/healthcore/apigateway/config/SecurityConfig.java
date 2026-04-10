package com.healthcore.apigateway.config;

import com.healthcore.apigateway.exception.TokenExpiredException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        // Specific protected session endpoints
                        .pathMatchers("/auth/sessions/**").authenticated()

                        // Everything else in auth (login/register) is public
                        .pathMatchers("/auth/**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        // Use the entry point to catch errors from the JWT filter
                        .authenticationEntryPoint(tokenValidationEntryPoint())
                )
                .build();
    }

    @Bean
    public ServerAuthenticationEntryPoint tokenValidationEntryPoint() {
        return (_, e) -> {
            // Check if the error is specifically due to an expired token
            if (e instanceof InvalidBearerTokenException && e.getMessage().contains("expired")) {
                // Return a Mono.error so it reaches your GlobalExceptionHandler
                return Mono.error(new TokenExpiredException("Session has expired. Please login again."));
            }
            // For other auth errors (invalid signature, malformed), just pass them through
            return Mono.error(e);
        };
    }
}