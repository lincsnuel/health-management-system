package com.healthcore.authservice.infrastructure.messaging.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OtpEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOtp(String phone, String email, String otp) {

        Map<String, Object> payload = new HashMap<>();
        payload.put("phone", phone);
        payload.put("email", email);
        payload.put("otp", otp);

        kafkaTemplate.send("send-otp", payload);
    }
}