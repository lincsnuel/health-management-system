package com.healthcore.workforceservice.infrastructure.adapter.input.messaging.listener;

import com.healthcore.workforceservice.domain.event.DepartmentCreatedEvent;
import com.healthcore.workforceservice.domain.event.DepartmentUpdatedEvent;
import com.healthcore.workforceservice.infrastructure.adapter.input.messaging.DepartmentEventConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class DepartmentKafkaListener {

    private final DepartmentEventConsumer consumer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "DepartmentCreatedEvent", groupId = "workforce-service-group")
    public void handleCreated(String payload) throws Exception {

        DepartmentCreatedEvent event =
                objectMapper.readValue(payload, DepartmentCreatedEvent.class);

        consumer.handleDepartmentCreated(event);
    }

    @KafkaListener(topics = "DepartmentUpdatedEvent", groupId = "workforce-service-group")
    public void handleUpdated(String payload) throws Exception {

        DepartmentUpdatedEvent event =
                objectMapper.readValue(payload, DepartmentUpdatedEvent.class);

        consumer.handleDepartmentUpdated(event);
    }
}