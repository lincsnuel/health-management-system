package com.healthcore.workforceservice.shared.event;

import java.util.List;

public interface DomainEventPublisher {

    void publish(List<DomainEvent> events);
}