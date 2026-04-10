package com.healthcore.authservice.application.patient.usecase;

import com.healthcore.authservice.application.common.dto.response.TokenResponse;
import com.healthcore.authservice.application.common.usecase.SessionManager;
import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.domain.otp.OtpService;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class VerifyPatientOtpUseCase {

    private final OtpService otpService;
    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    public Mono<TokenResponse> execute(String phone, String otp) {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> otpService.validateOtp(phone, otp)
                        .filter(valid -> valid)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid OTP")))
                        .then(userRepository.findByPhoneNumberAndTenantId(phone, tenantId))
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient record not found")))
                        .flatMap(sessionManager::createSession));
    }
}