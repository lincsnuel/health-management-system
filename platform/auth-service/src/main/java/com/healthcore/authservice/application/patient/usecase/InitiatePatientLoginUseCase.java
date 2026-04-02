package com.healthcore.authservice.application.patient.usecase;

import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.common.util.UsernameBuilder;
import com.healthcore.authservice.domain.otp.OtpService;
import com.healthcore.authservice.infrastructure.messaging.kafka.producer.OtpEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InitiatePatientLoginUseCase {

    private final OtpService otpService;
    private final OtpEventProducer otpEventProducer;

    public Mono<Void> execute(String phone, String email) {
        // 1. "Ask" the context for the tenantId
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID not found in context")))
                .flatMap(tenantId -> {
                    // 2. Now that we have the tenantId, build the key
                    String key = UsernameBuilder.build(phone, tenantId);

                    // 3. Generate OTP and Send via Kafka (Returning Mono<Void>)
                    // Note: If otpService.generateOtp is blocking (e.g. standard Redis/DB),
                    // wrap it in Mono.fromCallable. If it's already reactive, just call it.
                    // Use flatMap because generateOtp returns a Mono
                    return otpService.generateOtp(key)
                            .flatMap(otp -> Mono.fromRunnable(() ->
                                    otpEventProducer.sendOtp(phone, email, otp))
                            );
                })
                .then();
    }
}