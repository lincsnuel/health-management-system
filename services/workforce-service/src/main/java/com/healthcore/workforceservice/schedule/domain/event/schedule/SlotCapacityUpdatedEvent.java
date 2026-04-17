package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SlotCapacityUpdatedEvent extends DomainEvent {

    private final String departmentId;
    private final String slotId;

    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;

    private final int previousCapacity;
    private final int newCapacity;

    private final LocalDateTime occurredAt;

    public SlotCapacityUpdatedEvent(
            String departmentId,
            String slotId,
            LocalDate date,
            LocalTime start,
            LocalTime end,
            int previousCapacity,
            int newCapacity
    ) {
        if (newCapacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }

        this.departmentId = departmentId;
        this.slotId = slotId;
        this.date = date;
        this.start = start;
        this.end = end;
        this.previousCapacity = previousCapacity;
        this.newCapacity = newCapacity;
        this.occurredAt = LocalDateTime.now();
    }

    public String departmentId() { return departmentId; }
    public String slotId() { return slotId; }
    public LocalDate date() { return date; }
    public LocalTime start() { return start; }
    public LocalTime end() { return end; }
    public int previousCapacity() { return previousCapacity; }
    public int newCapacity() { return newCapacity; }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return slotId();
    }
}