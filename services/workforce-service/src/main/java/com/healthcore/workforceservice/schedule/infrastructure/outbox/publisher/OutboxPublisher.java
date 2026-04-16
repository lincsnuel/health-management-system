package com.healthcore.workforceservice.schedule.infrastructure.outbox.publisher;

import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.messaging.kafka.KafkaEventPublisher;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule.OutboxEventEntity;
import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final KafkaEventPublisher kafkaPublisher;

    // runs every 5 seconds
    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishOutboxEvents() {

        List<OutboxEventEntity> events = repository.findPendingEvents();

        for (OutboxEventEntity event : events) {

            try {
                kafkaPublisher.publish(event);

                event.setPublished(true);
                event.setLastTriedAt(LocalDateTime.now());

            } catch (Exception ex) {

                event.setRetryCount(event.getRetryCount() + 1);
                event.setLastTriedAt(LocalDateTime.now());

                log.error("Failed to publish outbox event {}",
                        event.getId(), ex);
            }
        }

        repository.saveAll(events);
    }
}