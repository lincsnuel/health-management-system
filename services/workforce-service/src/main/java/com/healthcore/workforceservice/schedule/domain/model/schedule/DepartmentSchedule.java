package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

public record DepartmentSchedule(
        UUID id,
        DayOfWeek dayOfWeek,
        List<TimeSlot> activeSlots,
        Map<TimeSlot, Integer> requiredStaffPerSlot
) {

    public DepartmentSchedule {
        Objects.requireNonNull(id);
        Objects.requireNonNull(dayOfWeek);
        Objects.requireNonNull(activeSlots);
        Objects.requireNonNull(requiredStaffPerSlot);

        if (activeSlots.isEmpty()) {
            throw new IllegalArgumentException("Active slots cannot be empty");
        }

        // Ensure all slots have capacity defined
        for (TimeSlot slot : activeSlots) {
            if (!requiredStaffPerSlot.containsKey(slot)) {
                throw new IllegalArgumentException("Missing capacity for slot: " + slot);
            }

            int capacity = requiredStaffPerSlot.get(slot);
            if (capacity < 0) {
                throw new IllegalArgumentException("Capacity cannot be negative");
            }
        }
    }

    public boolean isActiveAt(LocalDateTime time) {
        if (time.getDayOfWeek() != dayOfWeek) return false;

        return activeSlots.stream()
                .anyMatch(slot -> slot.contains(time.toLocalTime()));
    }

    public int getRequiredStaff(TimeSlot slot) {
        return requiredStaffPerSlot.getOrDefault(slot, 0);
    }
}