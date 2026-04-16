package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class DepartmentScheduleDefinedEvent extends DomainEvent {

    private final String departmentId;
    private final DayOfWeek day;
    private final List<String> slots;
    private final LocalDateTime occurredAt;

    public DepartmentScheduleDefinedEvent(String departmentId, DayOfWeek day, List<String> slots) {
        this.departmentId = departmentId;
        this.day = day;
        this.slots = slots;
        this.occurredAt = LocalDateTime.now();
    }

    public String departmentId() { return departmentId; }
    public DayOfWeek day() { return day; }
    public List<String> slots() { return slots; }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return "";
    }
}