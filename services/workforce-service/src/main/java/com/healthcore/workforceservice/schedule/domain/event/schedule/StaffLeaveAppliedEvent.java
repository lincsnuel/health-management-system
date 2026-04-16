package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
public class StaffLeaveAppliedEvent extends DomainEvent {

    private final UUID staffId;
    private final LocalDate start;
    private final LocalDate end;
    private final String leaveType;
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