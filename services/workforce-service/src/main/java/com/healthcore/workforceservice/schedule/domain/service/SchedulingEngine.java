package com.healthcore.workforceservice.schedule.domain.service;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.schedule.DepartmentScheduleAggregate;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffAllocationAggregate;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffShift;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentDaySchedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.RecurrencePattern;
import com.healthcore.workforceservice.schedule.domain.model.vo.SlotCapacity;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class SchedulingEngine {

    // =========================
    // AUTO ASSIGN (CORE ALGORITHM)
    // =========================

    public void autoAssign(
            DepartmentScheduleAggregate dept,
            StaffAllocationAggregate staff
    ) {

        Map<StaffId, Integer> workload = staff.workload();
        Set<StaffId> pool = staff.staffPool();

        // iterate only defined schedules (no null scans, no filtering)
        for (DepartmentDaySchedule daySchedule : dept.all()) {

            DayOfWeek day = daySchedule.dayOfWeek();

            // precompute shifts per slot once per day (avoid repeated full scans)
            Map<TimeSlot, Set<StaffId>> slotAssignments = indexAssignments(staff, daySchedule);

            for (TimeSlot slot : daySchedule.activeSlots()) {

                int required = daySchedule.requiredStaff(slot);

                Set<StaffId> assigned = slotAssignments.getOrDefault(slot, Set.of());

                int remaining = required - assigned.size();
                if (remaining <= 0) continue;

                List<StaffId> candidates = pool.stream()
                        .sorted(Comparator.comparingInt(s -> workload.getOrDefault(s, 0)))
                        .toList();

                for (StaffId candidate : candidates) {

                    if (remaining <= 0) break;
                    if (assigned.contains(candidate)) continue;

                    staff.assignShift(new StaffShift(
                            UUID.randomUUID(),
                            candidate,
                            resolveShiftType(slot),
                            slot,
                            RecurrencePattern.weekly(day)
                    ));

                    remaining--;
                }
            }
        }
    }

    // =========================
    // AVAILABILITY
    // =========================

    public DepartmentAvailability calculateAvailability(
            DepartmentScheduleAggregate dept,
            StaffAllocationAggregate staff,
            DepartmentId departmentId,
            LocalDate date
    ) {

        Optional<DepartmentDaySchedule> optional = dept.forDay(date.getDayOfWeek());

        if (optional.isEmpty()) {
            return emptyAvailability(departmentId, date);
        }

        DepartmentDaySchedule daySchedule = optional.get();

        List<SlotCapacity> capacities = new ArrayList<>(daySchedule.activeSlots().size());

        for (TimeSlot slot : daySchedule.activeSlots()) {
            capacities.add(buildCapacity(slot, daySchedule, staff, date));
        }

        return new DepartmentAvailability(
                UUID.randomUUID(),
                departmentId,
                date,
                capacities
        );
    }

    // =========================
    // HELPERS
    // =========================

    public ShiftType resolveShiftType(TimeSlot slot) {
        return slot.start().isBefore(LocalTime.NOON)
                ? ShiftType.MORNING
                : ShiftType.AFTERNOON;
    }

    private SlotCapacity buildCapacity(
            TimeSlot slot,
            DepartmentDaySchedule daySchedule,
            StaffAllocationAggregate staff,
            LocalDate date
    ) {

        int required = daySchedule.requiredStaff(slot);

        int assigned = 0;

        for (StaffShift shift : staff.shifts()) {

            if (!shift.appliesToDate(date)) continue;
            if (!shift.timeSlot().overlaps(slot)) continue;
            if (staff.staffAvailable(shift.staffId(), date)) continue;

            assigned++;
        }

        return new SlotCapacity(slot, required, assigned);
    }

    // =========================
    // OPTIMIZATION INDEX (IMPORTANT)
    // =========================

    private Map<TimeSlot, Set<StaffId>> indexAssignments(
            StaffAllocationAggregate staff,
            DepartmentDaySchedule daySchedule
    ) {

        Map<TimeSlot, Set<StaffId>> index = new HashMap<>();

        for (StaffShift shift : staff.shifts()) {

            for (TimeSlot slot : daySchedule.activeSlots()) {

                if (!shift.timeSlot().overlaps(slot)) continue;

                if (staff.staffAvailable(shift.staffId(), LocalDate.now())) continue;

                index.computeIfAbsent(slot, _ -> new HashSet<>())
                        .add(shift.staffId());
            }
        }

        return index;
    }

    private DepartmentAvailability emptyAvailability(
            DepartmentId departmentId,
            LocalDate date
    ) {

        return new DepartmentAvailability(
                UUID.randomUUID(),
                departmentId,
                date,
                Collections.emptyList()
        );
    }
}