package com.healthcore.authservice.application.staff.usecase;

import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakAdminClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompleteStaffRegistrationUseCase {

    private final KeycloakAdminClient keycloakAdminClient;

    /**
     * Completes registration reactively.
     * Sequences: Set Password -> Enable User.
     */
    public Mono<Void> execute(String email, String tenantId, String password) {

        // In Keycloak, the 'userId' is often the UUID, but if you configured
        // Keycloak to use the username as the identifier in your API calls:
        String username = UsernameBuilder.build(email, tenantId);

        log.info("Completing registration for user: {}", username);

        // 1. Set the password
        return keycloakAdminClient.setPassword(username, password)
                // 2. Once password is set, enable the user
                .then(Mono.defer(() -> {
                    log.info("Password set, now enabling user: {}", username);
                    return keycloakAdminClient.enableUser(username);
                }))
                .doOnSuccess(v -> log.info("Successfully completed registration for: {}", username))
                .doOnError(e -> log.error("Failed to complete registration for {}: {}", username, e.getMessage()));
    }
}