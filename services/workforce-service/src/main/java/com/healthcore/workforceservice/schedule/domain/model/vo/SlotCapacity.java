package com.healthcore.workforceservice.schedule.domain.model.vo;

public record SlotCapacity(
        TimeSlot slot,
        int requiredStaff,
        int assignedStaff
) {

    public int remainingCapacity() {
        return Math.max(0, requiredStaff - assignedStaff);
    }

    public boolean isUnderstaffed() {
        return assignedStaff < requiredStaff;
    }

    public boolean isOverstaffed() {
        return assignedStaff > requiredStaff;
    }

    public boolean isFull() {
        return assignedStaff >= requiredStaff;
    }
}