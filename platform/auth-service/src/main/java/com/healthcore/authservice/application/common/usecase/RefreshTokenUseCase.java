package com.healthcore.authservice.application.common.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakTokenClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RefreshTokenUseCase {
    private final KeycloakTokenClient tokenClient;

    public Mono<TokenResponse> execute(String refreshToken) {
        return tokenClient.refreshToken(refreshToken)
                .map(kcToken -> {
                    TokenResponse response = new TokenResponse();
                    response.setAccessToken(kcToken.getAccessToken());
                    response.setRefreshToken(kcToken.getRefreshToken());
                    response.setExpiresIn(kcToken.getExpiresIn());
                    return response;
                });
    }
}