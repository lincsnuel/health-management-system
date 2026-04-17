package com.healthcore.workforceservice.shared.outbox.messaging.kafka;

import com.healthcore.workforceservice.shared.outbox.messaging.EventPublisher;
import com.healthcore.workforceservice.shared.outbox.messaging.TopicResolver;
import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TopicResolver topicResolver;

    @Override
    public void publish(OutboxEventEntity event) {

        String topic = topicResolver.resolve(event.getEventType());

        kafkaTemplate.send(
                topic,
                event.getAggregateId(), // key = partitioning
                event.getPayload()
        );
    }
}