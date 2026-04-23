package com.healthcore.workforceservice.schedule.domain.model.vo;

import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import java.time.LocalDate;

public record Leave(StaffId staffId, LocalDate start, LocalDate end) {

    public boolean isOnLeave(StaffId staffId, LocalDate date) {
        return this.staffId.equals(staffId)
                && !date.isBefore(start)
                && !date.isAfter(end);
    }
}