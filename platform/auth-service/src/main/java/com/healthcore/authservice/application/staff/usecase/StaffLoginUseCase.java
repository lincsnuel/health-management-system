package com.healthcore.authservice.application.staff.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.staff.dto.request.StaffLoginRequest;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakTokenClient;
import com.healthcore.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class StaffLoginUseCase {

    private final KeycloakTokenClient tokenClient;

    public Mono<TokenResponse> execute(StaffLoginRequest request) {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> {
                    String username = UsernameBuilder.build(request.getEmail(), tenantId);
                    log.debug("Attempting staff login for username: {}", username);

                    return tokenClient.getToken(username, request.getPassword())
                            .map(this::mapToResponse);
                });
    }

    private TokenResponse mapToResponse(KeycloakTokenResponse kcToken) {
        TokenResponse response = new TokenResponse();
        response.setAccessToken(kcToken.getAccessToken());
        response.setRefreshToken(kcToken.getRefreshToken());
        response.setExpiresIn(kcToken.getExpiresIn());
        return response;
    }
}