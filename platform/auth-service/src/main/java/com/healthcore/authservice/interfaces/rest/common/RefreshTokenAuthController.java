package com.healthcore.authservice.interfaces.rest.common;

import com.healthcore.authservice.application.common.dto.request.RefreshTokenRequest;
import com.healthcore.authservice.application.common.dto.response.SessionResponse;
import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.common.usecase.LogoutUseCase;
import com.healthcore.authservice.application.common.usecase.RefreshTokenUseCase;
import com.healthcore.authservice.application.common.usecase.SessionManager;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.infrastructure.persistence.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RefreshTokenAuthController {

    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;
    private final SessionManager sessionManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostMapping("/refresh")
    public Mono<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return refreshTokenUseCase.execute(request.getRefreshToken())
                .map(ResponseEntity::ok);
    }

    /**
     * Logout from the current session.
     */
    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(@RequestBody RefreshTokenRequest request) {
        return logoutUseCase.execute(request.getRefreshToken())
                .thenReturn(ResponseEntity.noContent().build());
    }

    /**
     * List all active sessions for the authenticated user.
     */
    @GetMapping("/sessions")
    public Flux<SessionResponse> getActiveSessions() {
        return TenantContext.getUserId()
                .flatMapMany(refreshTokenRepository::findAllByUserId)
                .map(rt -> new SessionResponse(
                        rt.getId(),
                        rt.getDeviceName(),
                        rt.getIpAddress(),
                        rt.getUserAgent(),
                        rt.getLastUsedAt()
                ));
    }

    /**
     * Revoke a specific session (Remote Logout).
     */
    @DeleteMapping("/sessions/{sessionId}")
    public Mono<ResponseEntity<Void>> revokeSession(@PathVariable String sessionId) {
        return TenantContext.getUserId()
                .flatMap(userId -> sessionManager.revokeSession(sessionId, userId))
                .thenReturn(ResponseEntity.noContent().build());
    }

    /**
     * Logout from ALL devices.
     */
    @DeleteMapping("/sessions")
    public Mono<ResponseEntity<Void>> logoutAllDevices() {
        return TenantContext.getUserId()
                .flatMap(logoutUseCase::logoutAll)
                .thenReturn(ResponseEntity.noContent().build());
    }
}