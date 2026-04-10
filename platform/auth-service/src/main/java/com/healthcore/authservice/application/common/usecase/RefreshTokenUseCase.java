package com.healthcore.authservice.application.common.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.common.context.RequestMetadata;
import com.healthcore.authservice.infrastructure.persistence.entity.RefreshToken;
import com.healthcore.authservice.infrastructure.persistence.repository.RefreshTokenRepository;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import com.healthcore.authservice.infrastructure.security.TokenHasher;
import com.healthcore.authservice.infrastructure.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUseCase {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository repository;
    private final SessionManager sessionManager;
    private final UserRepository userRepository;

    public Mono<TokenResponse> execute(String refreshToken) {
        return jwtProvider.validateAndGetClaims(refreshToken)
                .onErrorMap(_ -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"))
                .flatMap(claims -> {
                    String jti = claims.getId();
                    return repository.findById(jti)
                            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session not found")))
                            .flatMap(stored -> validateAndRotate(stored, refreshToken));
                });
    }

    private Mono<TokenResponse> validateAndRotate(RefreshToken stored, String token) {
        // 1. Basic Integrity Check (Token Hash)
        // Prevents token reuse or manipulation if the DB is partially compromised
        if (!stored.getTokenHash().equals(TokenHasher.hash(token))) {
            log.warn("Refresh token reuse/mismatch detected for JTI: {}", stored.getId());
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid session state"));
        }

        // 2. Hijacking Check: Fingerprint Validation
        return RequestMetadata.get()
                .defaultIfEmpty(RequestMetadata.builder().build())
                .flatMap(currentMeta -> {

                    boolean isUserAgentChanged = stored.getUserAgent() != null &&
                            !stored.getUserAgent().equals(currentMeta.getUserAgent());

                    // IP check is vital for Healthcare environments (stationary clinic IPs)
                    boolean isIpChanged = stored.getIpAddress() != null &&
                            !stored.getIpAddress().equals(currentMeta.getIpAddress());

                    if (isUserAgentChanged || isIpChanged) {
                        log.error("SECURITY ALERT: Session hijacking suspected for User: {}. UA Match: {}, IP Match: {}",
                                stored.getUserId(), !isUserAgentChanged, !isIpChanged);

                        // ACTION: Revoke ALL sessions for this user for maximum safety
                        return repository.deleteByUserId(stored.getUserId())
                                .doOnTerminate(() -> {
                                    // Audit Log trigger
                                    log.info("Audit log: Suspicious activity for User ID {} attempted refresh from IP {} while session was bound to IP {}",
                                            stored.getUserId(), currentMeta.getIpAddress(), stored.getIpAddress());
                                })
                                .then(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Security compromise detected. Please login again.")));
                    }

                    // 3. Normal Rotation Logic (Burn and Replace)
                    assert stored.getId() != null;
                    return repository.deleteById(stored.getId())
                            .then(userRepository.findById(stored.getUserId()))
                            .flatMap(sessionManager::createSession);
                });
    }
}