package com.healthcore.workforceservice.application.command.event;

import com.healthcore.workforceservice.domain.event.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {

    void publish(List<DomainEvent> events);
}