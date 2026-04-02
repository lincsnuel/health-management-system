package com.healthcore.authservice.infrastructure.messaging.kafka.producer;

import com.healthcore.authservice.infrastructure.messaging.kafka.event.OtpEvent;
import com.healthcore.authservice.infrastructure.messaging.kafka.event.StaffInviteEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String OTP_TOPIC = "otp-topic";
    private static final String STAFF_INVITE_TOPIC = "staff-invite-topic";

    public void sendOtpEvent(OtpEvent event) {
        kafkaTemplate.send(OTP_TOPIC, event.getPhone(), event);
    }

    public void sendStaffInviteEvent(StaffInviteEvent event) {
        kafkaTemplate.send(STAFF_INVITE_TOPIC, event.getEmail(), event);
    }
}