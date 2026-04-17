package com.healthcore.workforceservice.shared.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = false)
public abstract class DomainEvent {

    private final String eventId;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
    }

    public abstract LocalDateTime occurredAt();

    /**
     * Aggregate identifier (used as Kafka key)
     */
    public abstract String getAggregateId();

    /**
     * REQUIRED for outbox partitioning & routing
     */
    public String getEventName() {
        return this.getClass().getSimpleName();
    }
}