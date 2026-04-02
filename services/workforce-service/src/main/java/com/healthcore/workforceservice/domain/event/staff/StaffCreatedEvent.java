package com.healthcore.workforceservice.domain.event.staff;

import com.healthcore.workforceservice.domain.event.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class StaffCreatedEvent extends DomainEvent {

    private final UUID staffId;
    private final String tenantId;
    private final String email;

    @Override
    public String getAggregateId() {
        return staffId.toString();
    }
}