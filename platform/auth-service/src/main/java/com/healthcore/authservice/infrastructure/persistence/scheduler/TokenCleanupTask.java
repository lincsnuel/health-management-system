package com.healthcore.authservice.infrastructure.persistence.scheduler;

import com.healthcore.authservice.infrastructure.persistence.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;

    // Runs every hour (3600000 ms)
    @Scheduled(fixedRate = 3600000)
    public void cleanExpiredTokens() {
        log.info("Starting scheduled cleanup of expired refresh tokens...");

        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now())
                .doOnSuccess(_ -> log.info("Expired tokens cleanup completed successfully."))
                .doOnError(e -> log.error("Failed to clean up expired tokens: {}", e.getMessage()))
                .subscribe(); // We must subscribe because this is a background task
    }
}