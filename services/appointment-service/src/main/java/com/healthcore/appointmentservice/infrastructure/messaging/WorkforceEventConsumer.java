package com.healthcore.appointmentservice.infrastructure.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WorkforceEventConsumer {

    @KafkaListener(topics = "workforce.events", groupId = "appointment-service")
    public void consume(String payload) {

        // Deserialize → route based on eventType
        System.out.println("Received: " + payload);

        // Example:
        // DepartmentAvailabilityUpdatedEvent → update availability cache
    }
}