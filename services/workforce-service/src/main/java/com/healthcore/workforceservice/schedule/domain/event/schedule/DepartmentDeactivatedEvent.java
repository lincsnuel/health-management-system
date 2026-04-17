package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
public class DepartmentDeactivatedEvent extends DomainEvent {

    private final String departmentId;

    // Optional reason for audit/logging
    private final String reason;

    // Indicates whether this is temporary or permanent
    private final boolean temporary;

    private final LocalDateTime occurredAt;


    public String departmentId() {
        return departmentId;
    }

    public String reason() {
        return reason;
    }

    public boolean temporary() {
        return temporary;
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return departmentId();
    }
}