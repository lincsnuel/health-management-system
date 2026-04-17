package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.event.schedule.StaffLeaveAppliedEvent;
import com.healthcore.workforceservice.schedule.domain.event.schedule.StaffLeaveCancelledEvent;
import com.healthcore.workforceservice.schedule.domain.event.schedule.StaffShiftAssignedEvent;
import com.healthcore.workforceservice.schedule.domain.event.schedule.StaffShiftRemovedEvent;
import com.healthcore.workforceservice.schedule.domain.exception.LeaveNotFoundException;
import com.healthcore.workforceservice.schedule.domain.exception.OverlapException;
import com.healthcore.workforceservice.schedule.domain.exception.ShiftNotFoundException;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.shared.event.DomainEvent;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

@Getter
public class StaffAllocationAggregate {

    private final List<StaffShift> shifts = new ArrayList<>();
    private final List<StaffLeave> leaves = new ArrayList<>();

    private final Set<StaffId> onboardedStaff = new HashSet<>();
    private final Map<StaffId, Integer> workload = new HashMap<>();

    private final Consumer<DomainEvent> eventPublisher;

    public StaffAllocationAggregate(Consumer<DomainEvent> eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    // =========================
    // BEHAVIOR
    // =========================

    public void onboard(StaffId staffId) {
        if (!onboardedStaff.add(staffId)) return;

        workload.putIfAbsent(staffId, 0);
    }

    public void assignShift(StaffShift shift) {
        validateNoOverlap(shift);

        shifts.add(shift);
        workload.merge(shift.staffId(), 1, Integer::sum);

        eventPublisher.accept(new StaffShiftAssignedEvent(
                shift.staffId().value(),
                shift.shiftType().name(),
                shift.timeSlot().start(),
                shift.timeSlot().end(),
                LocalDateTime.now()
        ));
    }

    public void removeShift(StaffId staffId, TimeSlot slot) {
        Optional<StaffShift> removedShift = shifts.stream()
                .filter(s -> s.staffId().equals(staffId) &&
                        s.timeSlot().equals(slot))
                .findFirst();

        if (removedShift.isEmpty()) {
            throw new ShiftNotFoundException("Shift not found");
        }

        shifts.remove(removedShift.get());

        workload.computeIfPresent(staffId, (_, v) -> Math.max(0, v - 1));

        eventPublisher.accept(new StaffShiftRemovedEvent(
                staffId.value(),
                slot.start(),
                slot.end(),
                LocalDateTime.now()
        ));
    }

    public void applyLeave(StaffLeave leave) {
        validateLeaveOverlap(leave);

        leaves.add(leave);

        eventPublisher.accept(new StaffLeaveAppliedEvent(
                leave.staffId().value(),
                leave.startDate(),
                leave.endDate(),
                leave.leaveType().name(),
                LocalDateTime.now()
        ));
    }

    public void cancelLeave(StaffId staffId,
                            LocalDate start,
                            LocalDate end) {

        Optional<StaffLeave> removed = leaves.stream()
                .filter(leave ->
                        leave.staffId().equals(staffId) &&
                                leave.startDate().equals(start) &&
                                leave.endDate().equals(end))
                .findFirst();

        if (removed.isEmpty()) {
            throw new LeaveNotFoundException("Leave record not found");
        }

        leaves.remove(removed.get());

        eventPublisher.accept(new StaffLeaveCancelledEvent(
                staffId.value(),
                start,
                end,
                LocalDateTime.now()
        ));
    }

    public void clearAndReplace(StaffAllocationAggregate other) {

        this.shifts.clear();
        this.shifts.addAll(other.shifts);

        this.leaves.clear();
        this.leaves.addAll(other.leaves);

        this.onboardedStaff.clear();
        this.onboardedStaff.addAll(other.onboardedStaff);

        this.workload.clear();
        this.workload.putAll(other.workload);
    }

    // =========================
    // QUERY METHODS
    // =========================

    public boolean staffAvailable(StaffId staffId, LocalDate date) {
        return leaves.stream()
                .anyMatch(l -> l.staffId().equals(staffId) && l.overlaps(date));
    }

    public List<StaffShift> shifts() {
        return Collections.unmodifiableList(shifts);
    }

    public Set<StaffId> staffPool() {
        return Collections.unmodifiableSet(onboardedStaff);
    }

    public Map<StaffId, Integer> workload() {
        return Collections.unmodifiableMap(workload);
    }

    // =========================
    // VALIDATION
    // =========================

    private void validateNoOverlap(StaffShift newShift) {
        boolean overlap = shifts.stream()
                .filter(s -> s.staffId().equals(newShift.staffId()))
                .anyMatch(s -> s.overlaps(newShift.timeSlot(), newShift.recurrence()));

        if (overlap) throw new OverlapException("Shift overlap");
    }

    private void validateLeaveOverlap(StaffLeave leave) {
        boolean overlap = leaves.stream()
                .filter(l -> l.staffId().equals(leave.staffId()))
                .anyMatch(l -> l.overlapsRange(leave.startDate(), leave.endDate()));

        if (overlap) throw new OverlapException("Leave overlap");
    }
}