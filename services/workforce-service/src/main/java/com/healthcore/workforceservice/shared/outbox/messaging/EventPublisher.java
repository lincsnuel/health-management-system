package com.healthcore.workforceservice.shared.outbox.messaging;

import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxEventEntity;

public interface EventPublisher {
    void publish(OutboxEventEntity event);
}