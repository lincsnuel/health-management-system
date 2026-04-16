package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.RecurrencePattern;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record StaffShift(
        UUID id,
        StaffId staffId,
        ShiftType shiftType,
        TimeSlot timeSlot,
        RecurrencePattern recurrence
) {
    public boolean appliesTo(LocalDateTime time) {
        return recurrence.appliesTo(time.toLocalDate())
                && timeSlot.contains(time.toLocalTime());
    }

    public boolean appliesToDate(LocalDate date) {
        return recurrence.appliesTo(date);
    }

    public boolean overlaps(TimeSlot slot, RecurrencePattern pattern) {
        return this.timeSlot.overlaps(slot)
                && this.recurrence.overlaps(pattern);
    }
}