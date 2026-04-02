package com.healthcore.authservice.domain.otp;

import com.healthcore.authservice.infrastructure.redis.OtpCacheRepository;
import com.healthcore.authservice.common.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpCacheRepository otpCacheRepository;
    private static final Duration OTP_TTL = Duration.ofMinutes(5);

    /**
     * Generate and store OTP.
     * Returns Mono<String> to ensure non-blocking execution.
     */
    public Mono<String> generateOtp(String key) {
        String otp = OtpGenerator.generate(6);

        // Save to Redis and then return the OTP string once the save is complete
        return otpCacheRepository.save(key, otp, OTP_TTL)
                .thenReturn(otp);
    }

    /**
     * Validate OTP.
     * Returns Mono<Boolean> and handles the one-time-use deletion logic reactively.
     */
    public Mono<Boolean> validateOtp(String key, String otp) {
        return otpCacheRepository.find(key) // returns Mono<String>
                .map(cachedOtp -> cachedOtp.equals(otp))
                .defaultIfEmpty(false)
                .flatMap(isValid -> {
                    if (isValid) {
                        // If valid, delete the key and return true
                        return otpCacheRepository.delete(key).thenReturn(true);
                    }
                    // If invalid, just return false
                    return Mono.just(false);
                });
    }
}