package com.healthcore.workforceservice.schedule.domain.service;

import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.vo.SlotCapacity;

import java.util.List;
import java.util.UUID;

public class AvailabilityPolicyDomainService {

    /**
     * Applies business policies on top of computed availability.
     * <p>
     * Policies:
     * - Minimum staff threshold
     * - Overbooking factor (on remaining capacity)
     * - Safety buffer (implicit via threshold)
     */
    public DepartmentAvailability applyPolicy(
            DepartmentAvailability base,
            int minStaffPerSlot,
            double overbookingFactor
    ) {

        List<SlotCapacity> adjustedSlots = base.slotCapacities().stream()
                .map(slot -> adjustSlot(slot, minStaffPerSlot, overbookingFactor))
                .toList();

        return new DepartmentAvailability(
                UUID.randomUUID(),
                base.departmentId(),
                base.date(),
                adjustedSlots
        );
    }

    private SlotCapacity adjustSlot(
            SlotCapacity slot,
            int minStaffPerSlot,
            double overbookingFactor
    ) {

        int remaining = slot.remainingCapacity();

        // Rule 1: Minimum staffing threshold
        if (slot.assignedStaff() < minStaffPerSlot) {
            return new SlotCapacity(
                    slot.slot(),
                    slot.requiredStaff(),
                    slot.requiredStaff() // force "full" → unavailable
            );
        }

        // Rule 2: Overbooking on REMAINING capacity (correct approach)
        int adjustedRemaining = (int) Math.floor(remaining * overbookingFactor);

        int newAssigned = slot.requiredStaff() - adjustedRemaining;

        return new SlotCapacity(
                slot.slot(),
                slot.requiredStaff(),
                Math.max(0, newAssigned)
        );
    }
}