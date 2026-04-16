package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events",
        indexes = {
                @Index(name = "idx_outbox_status_created", columnList = "published, createdAt")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEventEntity {

    @Id
    private UUID id;

    private String aggregateType;

    private UUID aggregateId;

    private String eventType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String payload;

    private LocalDateTime createdAt;

    private boolean published;

    private int retryCount;

    private LocalDateTime lastTriedAt;
}