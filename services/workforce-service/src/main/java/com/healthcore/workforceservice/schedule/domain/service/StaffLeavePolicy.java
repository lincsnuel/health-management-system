package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.vo.Leave;

import java.time.LocalDate;
import java.util.*;

public class LeaveAggregate {

    /**
     * Key: staffId
     * Value: sorted, non-overlapping leave intervals
     */
    private final Map<UUID, List<Leave>> leavesByStaff;

    public LeaveAggregate(List<Leave> leaves) {
        this.leavesByStaff = new HashMap<>();

        for (Leave leave : leaves) {
            addInternal(leave);
        }

        // Ensure all lists are sorted after construction
        normalizeAll();
    }

    // --------------------------------------------------
    // QUERY
    // --------------------------------------------------
    public boolean isOnLeave(UUID staffId, LocalDate date) {

        List<Leave> leaves = leavesByStaff.get(staffId);
        if (leaves == null || leaves.isEmpty()) return false;

        // Since sorted, we can break early
        for (Leave leave : leaves) {

            if (date.isBefore(leave.start())) {
                return false; // no need to continue
            }

            if (!date.isAfter(leave.end())) {
                return true;
            }
        }

        return false;
    }

    // --------------------------------------------------
    // COMMANDS
    // --------------------------------------------------
    public void addLeave(
            UUID staffId,
            LocalDate start,
            LocalDate end
    ) {

        validateRange(start, end);

        List<Leave> staffLeaves =
                leavesByStaff.computeIfAbsent(staffId, _ -> new ArrayList<>());

        // Check overlap efficiently (since sorted)
        for (Leave existing : staffLeaves) {

            if (end.isBefore(existing.start())) {
                break; // no overlap possible beyond this point
            }

            if (!start.isAfter(existing.end())) {
                throw new IllegalStateException("Overlapping leave exists");
            }
        }

        staffLeaves.add(new Leave(staffId, start, end));

        // Maintain sorted invariant
        staffLeaves.sort(Comparator.comparing(Leave::start));
    }

    public void removeLeave(
            UUID staffId,
            LocalDate start,
            LocalDate end
    ) {

        List<Leave> staffLeaves = leavesByStaff.get(staffId);
        if (staffLeaves == null) return;

        staffLeaves.removeIf(l ->
                l.start().equals(start) &&
                        l.end().equals(end)
        );

        if (staffLeaves.isEmpty()) {
            leavesByStaff.remove(staffId);
        }
    }

    // --------------------------------------------------
    // INTERNALS
    // --------------------------------------------------

    private void addInternal(Leave leave) {
        leavesByStaff
                .computeIfAbsent(leave.staffId(), _ -> new ArrayList<>())
                .add(leave);
    }

    private void normalizeAll() {
        for (List<Leave> staffLeaves : leavesByStaff.values()) {
            staffLeaves.sort(Comparator.comparing(Leave::start));
            validateNoOverlap(staffLeaves);
        }
    }

    private void validateNoOverlap(List<Leave> leaves) {

        for (int i = 0; i < leaves.size() - 1; i++) {

            Leave current = leaves.get(i);
            Leave next = leaves.get(i + 1);

            if (!current.end().isBefore(next.start())) {
                throw new IllegalStateException("Overlapping leaves detected during initialization");
            }
        }
    }

    private void validateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Leave dates cannot be null");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Invalid leave range");
        }
    }

    // --------------------------------------------------
    // OPTIONAL: EXPOSURE (READ-ONLY)
    // --------------------------------------------------
    public Map<UUID, List<Leave>> getLeavesByStaff() {
        return Collections.unmodifiableMap(leavesByStaff);
    }
}