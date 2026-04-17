package com.healthcore.workforceservice.shared.outbox.mapper;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxEventEntity;
import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxMapper {

    private final ObjectMapper objectMapper;

    public OutboxEventEntity toEntity(DomainEvent event) {
        try {
            return OutboxEventEntity.builder()
                    .id(UUID.randomUUID())
                    .aggregateId(event.getAggregateId())
                    .aggregateType(event.getEventName())
                    .eventType(event.getClass().getSimpleName())
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxStatus.PENDING)
                    .retryCount(0)
                    .createdAt(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Outbox serialization failed", e);
        }
    }
}