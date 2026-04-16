package com.healthcore.workforceservice.staff.domain.event.staff;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class StaffDeactivatedEvent extends DomainEvent {
    private final UUID staffId;
    private final String tenantId;
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