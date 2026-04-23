package com.healthcore.workforceservice.schedule.domain.model.schedule.entity;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.ShiftPattern;

import java.time.LocalDate;
import java.util.UUID;

public record StaffShiftAssignment(UUID staffId, ShiftPattern pattern) {

    public ShiftType resolveShift(LocalDate date) {
        return pattern.resolve(date);
    }
}