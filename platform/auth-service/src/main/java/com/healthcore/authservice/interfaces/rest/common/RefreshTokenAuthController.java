package com.healthcore.authservice.interfaces.rest.common;

import com.healthcore.authservice.application.common.dto.request.RefreshTokenRequest;
import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.common.usecase.RefreshTokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RefreshTokenAuthController {
    private final RefreshTokenUseCase refreshTokenUseCase;

    @PostMapping("/auth/refresh")
    public Mono<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return refreshTokenUseCase.execute(request.getRefreshToken())
                .map(ResponseEntity::ok)
                .onErrorResume(_ -> {
                    // If Keycloak rejects the refresh token (expired/invalid)
                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
                });
    }
}
