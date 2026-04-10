package com.healthcore.authservice.application.staff.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.common.usecase.SessionManager;
import com.healthcore.authservice.application.staff.dto.request.StaffLoginRequest;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.infrastructure.persistence.entity.User;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffLoginUseCase {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;
    private final PasswordEncoder passwordEncoder;

    public Mono<TokenResponse> execute(StaffLoginRequest request) {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> authenticate(request, tenantId))
                .flatMap(sessionManager::createSession);
    }

    private Mono<User> authenticate(StaffLoginRequest request, String tenantId) {
        return userRepository.findByEmailAndTenantId(request.getEmail(), tenantId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")))
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash()))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials")))
                .filter(User::isEnabled)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Account not activated")));
    }
}