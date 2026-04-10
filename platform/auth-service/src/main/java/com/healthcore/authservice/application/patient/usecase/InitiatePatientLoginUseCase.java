package com.healthcore.authservice.application.patient.usecase;

import com.healthcore.authservice.common.context.TenantContext;
import com.healthcore.authservice.domain.otp.OtpService;
import com.healthcore.authservice.infrastructure.messaging.kafka.producer.OtpEventProducer;
import com.healthcore.authservice.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class InitiatePatientLoginUseCase {

    private final OtpService otpService;
    private final OtpEventProducer otpEventProducer;
    private final UserRepository userRepository;

    public Mono<Void> execute(String phone, String email) {
        return TenantContext.getTenantId()
                .switchIfEmpty(Mono.error(new IllegalStateException("Tenant ID missing")))
                .flatMap(tenantId -> userRepository.existsByPhoneNumberAndTenantId(phone, tenantId)
                        .flatMap(exists -> {
                            if (!exists) {
                                return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone number not registered"));
                            }
                            return otpService.generateOtp(phone)
                                    .doOnNext(otp -> otpEventProducer.sendOtp(phone, email, otp));
                        }))
                .then();
    }
}