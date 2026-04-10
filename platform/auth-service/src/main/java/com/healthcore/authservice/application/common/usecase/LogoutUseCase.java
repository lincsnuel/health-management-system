package com.healthcore.authservice.application.common.usecase;

import com.healthcore.authservice.infrastructure.persistence.repository.RefreshTokenRepository;
import com.healthcore.authservice.infrastructure.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutUseCase {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    /**
     * Logs out a specific session using the refresh token's JTI.
     */
    public Mono<Void> execute(String refreshToken) {
        return jwtProvider.extractJti(refreshToken)
                .flatMap(refreshTokenRepository::deleteById)
                .doOnSuccess(_ -> log.info("Successfully logged out session"));
    }

    /**
     * "The Nuclear Option": Logs out all active sessions for a specific user.
     */
    public Mono<Void> logoutAll(String userId) {
        return refreshTokenRepository.deleteByUserId(userId)
                .doOnSuccess(_ -> log.info("Successfully revoked all sessions for user: {}", userId));
    }
}