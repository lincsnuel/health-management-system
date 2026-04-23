package com.healthcore.workforceservice.schedule.domain.model.vo;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;

import java.time.LocalDate;
import java.util.UUID;

public record StaffOverride(UUID staffId, LocalDate date, ShiftType shiftType) {

    public boolean applies(UUID staffId, LocalDate date) {
        return this.staffId.equals(staffId) && this.date.equals(date);
    }
}