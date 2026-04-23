package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.ShiftPattern;
import com.healthcore.workforceservice.schedule.domain.model.vo.StaffRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public record StaffScheduleAggregate(String departmentId, List<StaffRecord> staffRecords,
                                     List<StaffShiftAssignment> assignments, List<StaffOverride> overrides) {

    public ShiftType resolveShift(UUID staffId, LocalDate date) {

        // 1. Override takes precedence
        for (StaffOverride override : overrides) {
            if (override.applies(staffId, date)) {
                return override.shiftType();
            }
        }

        // 2. Pattern resolution
        return assignments.stream()
                .filter(a -> a.staffId().equals(staffId))
                .findFirst()
                .map(a -> a.resolveShift(date))
                .orElse(ShiftType.OFF);
    }

    public void assignDefaultShift(
            UUID staffId,
            ShiftPattern defaultPattern
    ) {
        boolean alreadyAssigned = assignments.stream()
                .anyMatch(a -> a.staffId().equals(staffId));

        if (alreadyAssigned) return;

        assignments.add(new StaffShiftAssignment(staffId, defaultPattern));
    }

    public void addStaffRecord(StaffRecord record) {

        boolean exists = staffRecords.stream()
                .anyMatch(s -> s.staffId().equals(record.staffId()));

        if (exists) {
            throw new IllegalStateException("Staff already exists in schedule");
        }

        if (!record.departmentId().equals(this.departmentId)) {
            throw new IllegalStateException("Invalid department assignment");
        }

        staffRecords.add(record);
    }

    public void assignOrUpdatePattern(UUID staffId, ShiftPattern pattern) {

        boolean exists = assignments.stream()
                .anyMatch(a -> a.staffId().equals(staffId));

        if (!exists) {
            assignments.add(new StaffShiftAssignment(staffId, pattern));
            return;
        }

        assignments.replaceAll(a ->
                a.staffId().equals(staffId)
                        ? new StaffShiftAssignment(staffId, pattern)
                        : a
        );
    }

    public void addOverride(
            UUID staffId,
            LocalDate date,
            ShiftType shiftType
    ) {

        // Prevent duplicate override for same date
        overrides.removeIf(o ->
                o.applies(staffId, date)
        );

        overrides.add(new StaffOverride(staffId, date, shiftType));
    }

    public Map<UUID, ShiftPattern> getAssignmentsMap() {
        return assignments.stream()
                .collect(Collectors.toMap(
                        StaffShiftAssignment::staffId,
                        StaffShiftAssignment::pattern
                ));
    }
}