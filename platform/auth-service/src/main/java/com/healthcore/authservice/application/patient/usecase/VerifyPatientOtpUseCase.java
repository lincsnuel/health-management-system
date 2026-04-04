package com.healthcore.authservice.application.patient.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.domain.otp.OtpService;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakAdminClient;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakTokenClient;
import com.healthcore.authservice.infrastructure.keycloak.dto.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyPatientOtpUseCase {

    private final OtpService otpService;
    private final KeycloakTokenClient tokenClient;
    private final KeycloakAdminClient adminClient;

    public Mono<TokenResponse> execute(String phone, String otp) {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> {
                    String username = UsernameBuilder.build(phone, tenantId);

                    // 1. Check Redis for OTP
                    return otpService.validateOtp(username, otp)
                            .flatMap(isValid -> {
                                if (!isValid) {
                                    return Mono.error(new ResponseStatusException(
                                            HttpStatus.UNAUTHORIZED, "Invalid OTP"));
                                }

                                // 2. Sequence: Find UUID -> Set OTP as Password -> Get JWT
                                return adminClient.findUserIdByUsername(username)
                                        .flatMap(userId -> adminClient.setPassword(userId, otp))
                                        .then(tokenClient.getToken(username, otp))
                                        .map(this::mapToResponse)
                                        .onErrorMap(e -> {
                                            log.error("Keycloak auth failed for {}: {}", username, e.getMessage());
                                            return new ResponseStatusException(
                                                    HttpStatus.INTERNAL_SERVER_ERROR, "Auth Provider Error");
                                        });
                            });
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