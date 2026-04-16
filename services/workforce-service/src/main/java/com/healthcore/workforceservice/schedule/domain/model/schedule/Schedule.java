package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.event.schedule.*;
import com.healthcore.workforceservice.schedule.domain.exception.*;
import com.healthcore.workforceservice.schedule.domain.model.enums.LeaveType;
import com.healthcore.workforceservice.schedule.domain.model.enums.ScheduleStrategy;
import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.*;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.Getter;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Schedule {

    private final ScheduleId scheduleId;
    private final DepartmentId departmentId;
    private final ScheduleStrategy strategy;

    private final List<DepartmentSchedule> departmentSchedules = new ArrayList<>();
    private final List<StaffShift> staffShifts = new ArrayList<>();
    private final List<StaffLeave> staffLeaves = new ArrayList<>();

    // Track onboarded staff (prevents duplicates)
    private final Set<StaffId> onboardedStaff = new HashSet<>();
    // Tracks number of assigned shifts per staff
    private final Map<StaffId, Integer> workloadIndex = new HashMap<>();

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Schedule(ScheduleId id,
                     DepartmentId departmentId,
                     ScheduleStrategy strategy) {
        this.scheduleId = id;
        this.departmentId = departmentId;
        this.strategy = strategy;
    }

    public static Schedule create(ScheduleId id,
                                  DepartmentId departmentId,
                                  ScheduleStrategy strategy) {
        return new Schedule(id, departmentId, strategy);
    }

    // =========================
// RECONSTRUCT (REHYDRATION)
// =========================
    public static Schedule reconstruct(
            ScheduleId scheduleId,
            DepartmentId departmentId,
            ScheduleStrategy strategy,
            List<DepartmentSchedule> departmentSchedules,
            List<StaffShift> staffShifts,
            List<StaffLeave> staffLeaves
    ) {

        Schedule schedule = new Schedule(scheduleId, departmentId, strategy);

        if (departmentSchedules != null) {
            schedule.departmentSchedules.addAll(departmentSchedules);
        }

        if (staffShifts != null) {
            schedule.staffShifts.addAll(staffShifts);
        }

        if (staffLeaves != null) {
            schedule.staffLeaves.addAll(staffLeaves);
        }

        // 🚨 VERY IMPORTANT
        schedule.clearDomainEvents();

        return schedule;
    }

    // =========================
    // DEFINE DEPARTMENT SCHEDULE
    // =========================
    public void defineDepartmentSchedule(
            DayOfWeek day,
            Map<TimeSlot, Integer> slotCapacities
    ) {

        if (slotCapacities == null || slotCapacities.isEmpty()) {
            throw new NullSlotsException("Slot capacities required");
        }

        validateNoOverlap(new ArrayList<>(slotCapacities.keySet()));

        departmentSchedules.removeIf(ds -> ds.dayOfWeek() == day);

        departmentSchedules.add(new DepartmentSchedule(
                UUID.randomUUID(),
                day,
                new ArrayList<>(slotCapacities.keySet()),
                slotCapacities
        ));

        rebalance();

        registerEvent(new DepartmentScheduleDefinedEvent(
                departmentId.value(),
                day,
                mapSlots(new ArrayList<>(slotCapacities.keySet()))
        ));
    }

    public void onboardStaff(StaffId staffId) {

        Objects.requireNonNull(staffId);

        if (onboardedStaff.contains(staffId)) {
            return;
        }

        // Add staff to pool
        workloadIndex.putIfAbsent(staffId, 0);

        // Run intelligent assignment
        autoAssignStaffToSlots(staffId);

        onboardedStaff.add(staffId);

        registerEvent(new StaffAutoScheduledEvent(
                staffId.value(),
                departmentId.value(),
                LocalDateTime.now()
        ));
    }

    private void autoAssignStaffToSlots(StaffId newStaff) {

        // Step 1: Ensure staff is tracked
        workloadIndex.putIfAbsent(newStaff, 0);

        // Step 2: Build candidate pool
        Set<StaffId> allStaff = new HashSet<>(workloadIndex.keySet());

        // Step 3: Iterate department schedules
        for (DepartmentSchedule ds : departmentSchedules) {

            for (TimeSlot slot : ds.activeSlots()) {

                int required = ds.requiredStaffPerSlot().getOrDefault(slot, 0);

                // Step 4: Find currently assigned staff for this slot
                Set<StaffId> assigned = staffShifts.stream()
                        .filter(s -> s.timeSlot().equals(slot))
                        .map(StaffShift::staffId)
                        .collect(Collectors.toSet());

                int remaining = required - assigned.size();

                if (remaining <= 0) continue; // slot full

                // Step 5: Sort staff by workload (ascending)
                List<StaffId> candidates = allStaff.stream()
                        .sorted(Comparator.comparingInt(workloadIndex::get))
                        .toList();

                // Step 6: Assign staff with the lowest workload
                for (StaffId candidate : candidates) {

                    if (remaining <= 0) break;

                    if (assigned.contains(candidate)) continue;

                    // Assign shift
                    staffShifts.add(new StaffShift(
                            UUID.randomUUID(),
                            candidate,
                            resolveShiftType(slot),
                            slot,
                            RecurrencePattern.weekly(ds.dayOfWeek())
                    ));

                    // Update workload
                    workloadIndex.put(candidate, workloadIndex.get(candidate) + 1);

                    remaining--;
                }
            }
        }
    }

    public void rebalance() {

        // Step 1: Reset workload
        workloadIndex.clear();

        // Step 2: Rebuild workload from MANUAL shifts only
        for (StaffShift shift : staffShifts) {
            workloadIndex.merge(shift.staffId(), 1, Integer::sum);
        }

        // Step 3: Build full staff pool
        Set<StaffId> allStaff = new HashSet<>(onboardedStaff);

        // Step 4: Re-run assignment globally
        for (DepartmentSchedule ds : departmentSchedules) {

            for (TimeSlot slot : ds.activeSlots()) {

                int required = ds.requiredStaffPerSlot().getOrDefault(slot, 0);

                // Already assigned (manual only at this point)
                Set<StaffId> assigned = staffShifts.stream()
                        .filter(s -> s.timeSlot().equals(slot))
                        .map(StaffShift::staffId)
                        .collect(Collectors.toSet());

                int remaining = required - assigned.size();

                if (remaining <= 0) continue;

                // Sort by workload
                List<StaffId> candidates = allStaff.stream()
                        .sorted(Comparator.comparingInt(s -> workloadIndex.getOrDefault(s, 0)))
                        .toList();

                for (StaffId candidate : candidates) {

                    if (remaining <= 0) break;

                    if (assigned.contains(candidate)) continue;

                    staffShifts.add(new StaffShift(
                            UUID.randomUUID(),
                            candidate,
                            resolveShiftType(slot),
                            slot,
                            RecurrencePattern.weekly(ds.dayOfWeek())
                    ));

                    workloadIndex.merge(candidate, 1, Integer::sum);

                    remaining--;
                }
            }
        }

        registerEvent(new ScheduleRebalancedEvent(
                departmentId.value(),
                LocalDateTime.now()
        ));
    }

    // =========================
    // ASSIGN SHIFT
    // =========================
    public void assignShift(
            StaffId staffId,
            ShiftType type,
            TimeSlot slot,
            RecurrencePattern recurrence
    ) {

        boolean overlap = staffShifts.stream()
                .filter(s -> s.staffId().equals(staffId))
                .anyMatch(s -> s.overlaps(slot, recurrence));

        if (overlap) {
            throw new OverlapException("Shift overlap");
        }

        staffShifts.add(new StaffShift(UUID.randomUUID(), staffId, type, slot, recurrence));

        registerEvent(new StaffShiftAssignedEvent(
                staffId.value(),
                departmentId.value(),
                type.name(),
                slot.start(),
                slot.end(),
                LocalDateTime.now()
        ));
    }

    // =========================
// REMOVE SHIFT
// =========================
    public void removeShift(StaffId staffId, TimeSlot slot) {

        Objects.requireNonNull(staffId, "StaffId required");
        Objects.requireNonNull(slot, "TimeSlot required");

        boolean removed = staffShifts.removeIf(shift ->
                shift.staffId().equals(staffId) &&
                        shift.timeSlot().equals(slot)
        );

        if (!removed) {
            throw new ShiftNotFoundException("Shift not found for removal");
        }

        registerEvent(new StaffShiftRemovedEvent(
                staffId.value(),
                departmentId.value(),
                slot.start(),
                slot.end(),
                LocalDateTime.now()
        ));
    }

    // =========================
    // APPLY LEAVE
    // =========================
    public void applyLeave(StaffId staffId,
                           LocalDate start,
                           LocalDate end,
                           LeaveType type) {

        if (end.isBefore(start)) {
            throw new InvalidDateRangeException("Invalid leave range");
        }

        boolean overlap = staffLeaves.stream()
                .filter(l -> l.staffId().equals(staffId))
                .anyMatch(l -> l.overlapsRange(start, end));

        if (overlap) {
            throw new OverlapException("Leave overlap");
        }

        staffLeaves.add(new StaffLeave(UUID.randomUUID(), staffId, start, end, type));

        rebalance();

        registerEvent(new StaffLeaveAppliedEvent(
                staffId.value(), start, end, type.name(), LocalDateTime.now()
        ));
    }

    // =========================
// CANCEL LEAVE
// =========================
    public void cancelLeave(StaffId staffId,
                            LocalDate start,
                            LocalDate end) {

        Objects.requireNonNull(staffId);
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);

        boolean removed = staffLeaves.removeIf(leave ->
                leave.staffId().equals(staffId) &&
                        leave.startDate().equals(start) &&
                        leave.endDate().equals(end)
        );

        if (!removed) {
            throw new LeaveNotFoundException("Leave record not found");
        }

        registerEvent(new StaffLeaveCancelledEvent(
                staffId.value(),
                start,
                end,
                LocalDateTime.now()
        ));
    }

    // =========================
// GET AVAILABLE STAFF (REAL-TIME)
// =========================
    public List<StaffId> getAvailableStaff(LocalDateTime time) {

        // 1. Check if department is active at that time
        boolean active = departmentSchedules.stream()
                .anyMatch(ds -> ds.isActiveAt(time));

        if (!active) {
            return List.of();
        }

        LocalDate date = time.toLocalDate();

        return staffShifts.stream()
                // shift applies to that date + time
                .filter(shift -> shift.appliesTo(time))

                // remove duplicates
                .map(StaffShift::staffId)
                .distinct()

                // remove staff on leave
                .filter(staffId -> isStaffAvailable(staffId, date))

                .toList();
    }

    // =========================
    // CORE AVAILABILITY ENGINE
    // =========================
    public DepartmentAvailability calculateAvailability(LocalDate date) {

        List<DepartmentSchedule> schedulesForDay = departmentSchedules.stream()
                .filter(ds -> ds.dayOfWeek() == date.getDayOfWeek())
                .toList();

        if (schedulesForDay.isEmpty()) {
            return new DepartmentAvailability(
                    UUID.randomUUID(),
                    departmentId,
                    date,
                    List.of()
            );
        }

        List<SlotCapacity> capacities = new ArrayList<>();

        for (DepartmentSchedule ds : schedulesForDay) {

            for (TimeSlot slot : ds.activeSlots()) {

                int required = ds.requiredStaffPerSlot().getOrDefault(slot, 0);

                int assigned = (int) staffShifts.stream()
                        .filter(shift -> shift.appliesToDate(date))
                        .filter(shift -> shift.timeSlot().overlaps(slot))
                        .map(StaffShift::staffId)
                        .distinct()
                        .filter(staffId -> isStaffAvailable(staffId, date))
                        .count();

                capacities.add(new SlotCapacity(slot, required, assigned));
            }
        }

        return new DepartmentAvailability(
                UUID.randomUUID(),
                departmentId,
                date,
                capacities
        );
    }

    public DepartmentAvailabilityCalculatedEvent calculateAndRaiseAvailability(LocalDate date) {

        DepartmentAvailability availability = calculateAvailability(date);

        Map<ShiftType, Integer> mapped = mapToShiftType(
                availability.slotCapacities()
                        .stream()
                        .collect(HashMap::new,
                                (map, sc) -> map.put(sc.slot(), sc.assignedStaff()),
                                HashMap::putAll)
        );

        return new DepartmentAvailabilityCalculatedEvent(
                departmentId.value(),
                date,
                mapped,
                LocalDateTime.now()
        );
    }

    // =========================
    // HELPERS
    // =========================
    private boolean isStaffAvailable(StaffId staffId, LocalDate date) {
        return staffLeaves.stream()
                .noneMatch(l -> l.staffId().equals(staffId) && l.overlaps(date));
    }

    private Map<ShiftType, Integer> mapToShiftType(Map<TimeSlot, Integer> slotMap) {

        Map<ShiftType, Integer> result = new EnumMap<>(ShiftType.class);

        for (var entry : slotMap.entrySet()) {

            ShiftType type = resolveShiftType(entry.getKey());

            result.merge(type, entry.getValue(), Integer::sum);
        }

        return result;
    }

    private ShiftType resolveShiftType(TimeSlot slot) {
        return slot.start().isBefore(LocalTime.NOON)
                ? ShiftType.MORNING
                : ShiftType.AFTERNOON;
    }

    private void validateNoOverlap(List<TimeSlot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            for (int j = i + 1; j < slots.size(); j++) {
                if (slots.get(i).overlaps(slots.get(j))) {
                    throw new DomainException("Overlapping slots");
                }
            }
        }
    }

    private List<String> mapSlots(List<TimeSlot> slots) {
        return slots.stream()
                .map(s -> s.start() + "-" + s.end())
                .toList();
    }

    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}