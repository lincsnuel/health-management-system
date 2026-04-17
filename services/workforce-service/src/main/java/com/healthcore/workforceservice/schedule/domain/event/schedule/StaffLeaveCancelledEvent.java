package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@RequiredArgsConstructor
@Getter
public class StaffLeaveCancelledEvent extends DomainEvent {

    private final UUID staffId;
    private final LocalDate start;
    private final LocalDate end;
    private final LocalDateTime occurredAt;

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return staffId.toString();
    }
}