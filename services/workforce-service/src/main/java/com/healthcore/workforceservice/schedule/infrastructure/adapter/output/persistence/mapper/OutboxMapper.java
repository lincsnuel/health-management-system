package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.mapper;

import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule.OutboxEventEntity;
import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class OutboxMapper {

    private final ObjectMapper objectMapper;

    public OutboxEventEntity from(DomainEvent event,
                                  String aggregateType,
                                  UUID aggregateId) {

        try {
            return OutboxEventEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateType(aggregateType)
                    .aggregateId(aggregateId)
                    .eventType(event.getClass().getSimpleName())
                    .payload(objectMapper.writeValueAsString(event))
                    .createdAt(LocalDateTime.now())
                    .published(false)
                    .retryCount(0)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}