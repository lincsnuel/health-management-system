package com.healthcore.workforceservice.domain.event.staff;

import com.healthcore.workforceservice.domain.event.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class StaffActivatedEvent extends DomainEvent {

    private final UUID staffId;
    private final String tenantId;

    @Override
    public String getAggregateId() {
        return staffId.toString();
    }

}