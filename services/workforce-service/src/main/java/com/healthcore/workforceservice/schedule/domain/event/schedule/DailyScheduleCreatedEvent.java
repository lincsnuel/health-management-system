package com.healthcore.workforceservice.schedule.domain.event.schedule;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class DailyScheduleCreatedEvent extends DomainEvent {

    private final String departmentId;
    private final LocalDate date;

    // Time slots created for this specific day
    private final List<Slot> slots;

    private final LocalDateTime occurredAt;

    public String departmentId() {
        return departmentId;
    }

    public LocalDate date() {
        return date;
    }

    public List<Slot> slots() {
        return slots;
    }

    @Override
    public LocalDateTime occurredAt() {
        return occurredAt;
    }

    @Override
    public String getAggregateId() {
        return departmentId;
    }

    /**
     * @param start ISO string safer for serialization
     */ // =========================
        // INNER DTO (IMMUTABLE)
        // =========================
        public record Slot(String slotId, String start, String end, int capacity) {

    }
}