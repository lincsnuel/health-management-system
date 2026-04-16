package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class StaffUnavailableEvent extends DomainEvent {

    private final UUID staffId;
    private final LocalDateTime occurredAt;

    public UUID staffId() {
        return staffId;
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return "";
    }
}