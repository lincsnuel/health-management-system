package com.healthcore.workforceservice.shared.outbox.persistence.repository;

import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxEventEntity;
import com.healthcore.workforceservice.shared.outbox.persistence.entity.OutboxStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxEventEntity, UUID> {

    List<OutboxEventEntity> findTop100ByAggregateTypeAndStatusOrderByCreatedAtAsc(
            String aggregateType,
            OutboxStatus status
    );
}