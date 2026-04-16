package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule.OutboxEventEntity;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEventEntity, UUID> {

    @Query("""
        SELECT o FROM OutboxEventEntity o
        WHERE o.published = false
        ORDER BY o.createdAt ASC
        LIMIT 100
    """)
    List<OutboxEventEntity> findPendingEvents();
}