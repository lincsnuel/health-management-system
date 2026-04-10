package com.healthcore.authservice.infrastructure.persistence.repository;

import com.healthcore.authservice.infrastructure.persistence.entity.RefreshToken;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

public interface RefreshTokenRepository extends R2dbcRepository<RefreshToken, String> {

    Mono<RefreshToken> findByUserIdAndTenantId(String userId, String tenantId);

    Mono<Void> deleteByUserIdAndTenantId(String userId, String tenantId);

    // Multi-device listing
    // Used for listing all active devices for a specific user
    Flux<RefreshToken> findAllByUserId(String userId);

    // Used to check the session limit
    Mono<Long> countByUserId(String userId);

    // Finds the oldest session to "kick out" when limit is reached
    Mono<RefreshToken> findFirstByUserIdOrderByCreatedDateAsc(String userId);

    // Used for the "Logout All Devices" nuclear option
    Mono<Void> deleteByUserId(String userId);

    // Cleanup task (Janitor)
    Mono<Void> deleteByExpiryDateBefore(Instant now);
}