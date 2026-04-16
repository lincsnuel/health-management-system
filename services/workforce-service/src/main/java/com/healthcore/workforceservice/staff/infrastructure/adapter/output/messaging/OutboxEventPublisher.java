package com.healthcore.workforceservice.staff.infrastructure.adapter.output.messaging;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.OutboxEventEntity;
import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository.OutboxEventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher implements DomainEventPublisher {

    private final OutboxEventJpaRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public void publish(List<DomainEvent> events) {

        for (DomainEvent event : events) {
            try {
                String payload = objectMapper.writeValueAsString(event);

                OutboxEventEntity outbox = new OutboxEventEntity(
                        event.getAggregateId(), // FIX: enforce method in base class
                        event.getClass().getSimpleName(),
                        event.getClass().getName(),
                        payload
                );

                repository.save(outbox);

            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize event", e);
            }
        }
    }
}