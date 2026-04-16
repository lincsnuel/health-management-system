package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.LeaveType;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import java.time.LocalDate;
import java.util.UUID;

public record StaffLeave(
        UUID id,
        StaffId staffId,
        LocalDate startDate,
        LocalDate endDate,
        LeaveType leaveType
) {
    public boolean overlaps(LocalDate date) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    public boolean overlapsRange(LocalDate start, LocalDate end) {
        return !(end.isBefore(startDate) || start.isAfter(endDate));
    }
}