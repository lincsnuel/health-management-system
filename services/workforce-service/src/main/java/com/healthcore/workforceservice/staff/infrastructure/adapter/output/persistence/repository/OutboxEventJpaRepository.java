package com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.repository;

import com.healthcore.workforceservice.staff.infrastructure.adapter.output.persistence.entity.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventJpaRepository extends JpaRepository<OutboxEventEntity, String> {

    List<OutboxEventEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}