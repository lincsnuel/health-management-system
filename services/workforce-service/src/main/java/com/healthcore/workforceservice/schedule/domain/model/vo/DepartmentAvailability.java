package com.healthcore.workforceservice.schedule.domain.model.vo;

import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DepartmentAvailability(
        UUID id,
        DepartmentId departmentId,
        LocalDate date,
        List<SlotCapacity> slotCapacities
) {

    public boolean isOperational() {
        return !slotCapacities.isEmpty();
    }

    public boolean isStaffed() {
        return slotCapacities.stream()
                .anyMatch(c -> c.assignedStaff() > 0);
    }

    public boolean hasCapacity() {
        return slotCapacities.stream()
                .anyMatch(c -> c.remainingCapacity() > 0);
    }

    public int totalRequired() {
        return slotCapacities.stream()
                .mapToInt(SlotCapacity::requiredStaff)
                .sum();
    }

    public boolean hasAnyAssignments() {
        return slotCapacities.stream()
                .anyMatch(c -> c.assignedStaff() > 0);
    }

    public int totalAssigned() {
        return slotCapacities.stream()
                .mapToInt(SlotCapacity::assignedStaff)
                .sum();
    }
}