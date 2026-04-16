package com.healthcore.workforceservice.shared.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class DepartmentCreatedEvent extends DomainEvent {
    private final String departmentId;
    private final String tenantId;
    private final String name;
    private final boolean active;
    private final LocalDateTime  occurredAt;

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return departmentId;
    }
}
