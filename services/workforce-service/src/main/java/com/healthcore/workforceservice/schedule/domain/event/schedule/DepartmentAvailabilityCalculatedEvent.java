package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
public class DepartmentAvailabilityCalculatedEvent extends DomainEvent {

    private final String departmentId;
    private final LocalDate date;
    private final Map<ShiftType, Integer> slotCapacities;
    private final LocalDateTime occurredAt;

    public String departmentId() { return departmentId; }
    public LocalDate date() { return date; }
    public Map<ShiftType, Integer> slotCapacities() { return slotCapacities; }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return departmentId;
    }
}