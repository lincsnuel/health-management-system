package com.healthcore.workforceservice.infrastructure.adapter.output.messaging;

import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.entity.OutboxEventEntity;
import com.healthcore.workforceservice.infrastructure.adapter.output.persistence.repository.OutboxEventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxEventJpaRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void process() {

        List<OutboxEventEntity> events =
                repository.findTop50ByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEventEntity event : events) {
            try {
                kafkaTemplate.send(event.getEventType(), event.getPayload());

                event.markPublished();

            } catch (Exception e) {
                event.markFailed();
            }

            repository.save(event);
        }
    }
}