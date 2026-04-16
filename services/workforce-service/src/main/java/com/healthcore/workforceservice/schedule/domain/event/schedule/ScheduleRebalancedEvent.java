package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;

import java.time.LocalDateTime;

public class ScheduleRebalancedEvent extends DomainEvent {

    private final String departmentId;
    private final LocalDateTime occurredAt;

    public ScheduleRebalancedEvent(String departmentId, LocalDateTime occurredAt) {
        this.departmentId = departmentId;
        this.occurredAt = occurredAt;
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return departmentId;
    }
}