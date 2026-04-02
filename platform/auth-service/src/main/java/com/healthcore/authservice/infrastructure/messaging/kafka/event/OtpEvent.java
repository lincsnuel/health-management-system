package com.healthcore.authservice.infrastructure.messaging.kafka.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpEvent {
    private String phone;        // patient phone
    private String otp;          // generated OTP
    private long expiresIn;      // TTL in seconds
}