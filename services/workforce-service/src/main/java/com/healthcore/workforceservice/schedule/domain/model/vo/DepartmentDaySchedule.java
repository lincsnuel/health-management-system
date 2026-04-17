package com.healthcore.workforceservice.schedule.domain.model.vo;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record DepartmentDaySchedule(
        DayOfWeek dayOfWeek,
        List<TimeSlot> activeSlots,
        Map<TimeSlot, Integer> requiredStaffPerSlot
) {

    public DepartmentDaySchedule {
        Objects.requireNonNull(dayOfWeek);
        Objects.requireNonNull(activeSlots);
        Objects.requireNonNull(requiredStaffPerSlot);

        if (activeSlots.isEmpty()) {
            throw new IllegalArgumentException("Active slots cannot be empty");
        }

        for (TimeSlot slot : activeSlots) {
            if (!requiredStaffPerSlot.containsKey(slot)) {
                throw new IllegalArgumentException("Missing capacity for slot: " + slot);
            }

            if (requiredStaffPerSlot.get(slot) < 0) {
                throw new IllegalArgumentException("Capacity cannot be negative");
            }
        }
    }

    public boolean isActiveAt(LocalDateTime time) {
        if (time.getDayOfWeek() != dayOfWeek) return false;

        return activeSlots.stream()
                .anyMatch(slot -> slot.contains(time.toLocalTime()));
    }

    public int requiredStaff(TimeSlot slot) {
        return requiredStaffPerSlot.getOrDefault(slot, 0);
    }
}