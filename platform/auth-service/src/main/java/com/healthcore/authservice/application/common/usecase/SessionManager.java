package com.healthcore.authservice.application.common.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.common.context.RequestMetadata;
import com.healthcore.authservice.infrastructure.persistence.entity.RefreshToken;
import com.healthcore.authservice.infrastructure.persistence.entity.User;
import com.healthcore.authservice.infrastructure.persistence.repository.RefreshTokenRepository;
import com.healthcore.authservice.infrastructure.security.TokenHasher;
import com.healthcore.authservice.infrastructure.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionManager {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public Mono<TokenResponse> createSession(User user) {
        // 1. Enforce Session Limit (Max 5)
        return refreshTokenRepository.countByUserId(user.getId())
                .flatMap(count -> {
                    if (count >= 5) {
                        return refreshTokenRepository.findFirstByUserIdOrderByCreatedDateAsc(user.getId())
                                .flatMap(refreshTokenRepository::delete);
                    }
                    return Mono.empty();
                })
                .then(generateAndSave(user));
    }

    private Mono<TokenResponse> generateAndSave(User user) {
        return Mono.zip(
                jwtProvider.generateAccessToken(user.getId(), user.getTenantId(), user.getUserType().name(), List.of(user.getUserType().name())),
                jwtProvider.generateRefreshToken(user.getId(), user.getTenantId()),
                RequestMetadata.get().defaultIfEmpty(RequestMetadata.builder().build())
        ).flatMap(tuple -> {
            String accessToken = tuple.getT1();
            String refreshToken = tuple.getT2();
            RequestMetadata meta = tuple.getT3();

            return jwtProvider.validateAndGetClaims(refreshToken)
                    .flatMap(claims -> {
                        RefreshToken rt = RefreshToken.builder()
                                .id(claims.getId())
                                .userId(user.getId())
                                .tenantId(user.getTenantId())
                                .tokenHash(TokenHasher.hash(refreshToken))
                                .deviceId(meta.getDeviceId())
                                .deviceName(meta.getDeviceName())
                                .ipAddress(meta.getIpAddress())
                                .userAgent(meta.getUserAgent())
                                .lastUsedAt(Instant.now())
                                .expiryDate(claims.getExpiration().toInstant())
                                .createdDate(Instant.now())
                                .isNew(true)
                                .build();

                        return refreshTokenRepository.save(rt)
                                .thenReturn(new TokenResponse(accessToken, refreshToken, (int) JwtProvider.ACCESS_TOKEN_EXP.toSeconds()));
                    });
        });
    }

    public Mono<Void> revokeSession(String sessionId, String currentUserId) {
        return refreshTokenRepository.findById(sessionId)
                .filter(rt -> rt.getUserId().equals(currentUserId))
                .flatMap(refreshTokenRepository::delete)
                .then();
    }
}