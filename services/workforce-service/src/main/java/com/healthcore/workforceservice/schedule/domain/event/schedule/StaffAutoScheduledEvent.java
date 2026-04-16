package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class StaffAutoScheduledEvent extends DomainEvent {

    private final UUID staffId;
    private final String departmentId;
    private final LocalDateTime occurredAt;

    public StaffAutoScheduledEvent(UUID staffId, String departmentId, LocalDateTime occurredAt) {
        this.staffId = staffId;
        this.departmentId = departmentId;
        this.occurredAt = occurredAt;
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return staffId.toString();
    }
}