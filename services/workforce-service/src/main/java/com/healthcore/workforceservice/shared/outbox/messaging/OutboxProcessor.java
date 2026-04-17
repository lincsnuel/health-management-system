package com.healthcore.workforceservice.shared.outbox.messaging;

import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxEventEntity;
import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxStatus;
import com.healthcore.workforceservice.shared.outbox.persistence.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class OutboxProcessor {

    private final OutboxRepository repository;
    private final EventPublisher publisher;
    private final String aggregateType;

    @Transactional
    public void process() {

        List<OutboxEventEntity> events =
                repository.findTop100ByAggregateTypeAndStatusOrderByCreatedAtAsc(
                        aggregateType,
                        OutboxStatus.PENDING
                );

        for (OutboxEventEntity event : events) {
            try {
                publisher.publish(event);
                event.markPublished();

            } catch (Exception ex) {
                event.markFailed();

                log.error("Outbox publish failed: {}", event.getId(), ex);
            }
        }

        repository.saveAll(events);
    }
}