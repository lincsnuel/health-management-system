package com.healthcore.authservice.application.staff.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.common.usecase.SessionManager;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class CompleteStaffRegistrationUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionManager sessionManager; // Injected for automatic login

    /**
     * Completes registration and returns tokens so the user is logged in immediately.
     */
    public Mono<TokenResponse> execute(String email, String tenantId, String password) {
        return userRepository.findByEmailAndTenantId(email, tenantId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff member not found")))
                .flatMap(user -> {
                    // 1. Update user security details
                    user.setPasswordHash(passwordEncoder.encode(password));
                    user.setEnabled(true);
                    user.setAsExisting();

                    // 2. Persist the updated user
                    return userRepository.save(user);
                })
                // 3. Hand off to SessionManager for token generation
                // SessionManager will automatically pick up Metadata from the Filter!
                .flatMap(sessionManager::createSession)
                .doOnSuccess(_ -> log.info("Staff registration completed and session created for: {}", email));
    }
}