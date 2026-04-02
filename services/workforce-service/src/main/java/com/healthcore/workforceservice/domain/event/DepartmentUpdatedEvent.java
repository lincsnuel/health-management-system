package com.healthcore.workforceservice.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
public class DepartmentUpdatedEvent extends DomainEvent {
    private final String departmentId;
    private final String name;
    private final boolean active;

    @Override
    public String getAggregateId() {
        return departmentId;
    }
}
