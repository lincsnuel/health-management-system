package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@RequiredArgsConstructor
public class StaffShiftRemovedEvent extends DomainEvent {

    private final UUID staffId;
    private final String departmentId;
    private final LocalTime start;
    private final LocalTime end;
    private final LocalDateTime occurredAt;

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return "";
    }
}