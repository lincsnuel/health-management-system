package com.healthcore.authservice.application.patient.usecase;

import com.healthcore.authservice.application.patient.dto.response.TokenResponse;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.domain.otp.OtpService;
import com.healthcore.authservice.infrastructure.keycloak.KeycloakTokenClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VerifyPatientOtpUseCase {

    private final OtpService otpService;
    private final KeycloakTokenClient tokenClient;

    public Mono<TokenResponse> execute(String phone, String otp) {
        // 1. Get Tenant ID reactively
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> {
                    String key = UsernameBuilder.build(phone, tenantId);
                    String username = UsernameBuilder.build(phone, tenantId);

                    // 2. Validate OTP reactively
                    return otpService.validateOtp(key, otp)
                            .flatMap(isValid -> {
                                if (!isValid) {
                                    return Mono.error(new RuntimeException("Invalid OTP"));
                                }

                                // 3. Exchange for Keycloak token
                                return tokenClient.getToken(username, otp)
                                        .map(kcToken -> {
                                            TokenResponse tokenResponse = new TokenResponse();
                                            tokenResponse.setAccessToken(kcToken.getAccessToken());
                                            tokenResponse.setRefreshToken(kcToken.getRefreshToken());
                                            tokenResponse.setExpiresIn(kcToken.getExpiresIn());
                                            return tokenResponse;
                                        });
                            });
                });
    }
}