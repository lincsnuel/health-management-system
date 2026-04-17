package com.healthcore.workforceservice.shared.outbox.service;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import com.healthcore.workforceservice.shared.outbox.mapper.OutboxMapper;
import com.healthcore.workforceservice.shared.outbox.persistence.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository repository;
    private final OutboxMapper mapper;

    public void saveEvents(List<DomainEvent> events) {
        events.forEach(event -> repository.save(mapper.toEntity(event)));
    }
}