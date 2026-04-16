package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
public class OutboxEventEntity {

    @Id
    private String id;

    private String aggregateId;
    private String aggregateType;
    private String eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String status; // PENDING, PUBLISHED, FAILED

    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    protected OutboxEventEntity() {}

    public OutboxEventEntity(
            String aggregateId,
            String aggregateType,
            String eventType,
            String payload
    ) {
        this.id = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = eventType;
        this.payload = payload;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    public void markPublished() {
        this.status = "PUBLISHED";
        this.publishedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = "FAILED";
    }
    // getters + setters
}