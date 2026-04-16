package com.healthcore.workforceservice.schedule.application.command.model;

import java.time.LocalDate;

import com.healthcore.workforceservice.schedule.domain.model.enums.LeaveType;
import lombok.Getter;

@Getter
public class StaffLeaveRequest {

    private LocalDate start;
    private LocalDate end;
    private LeaveType leaveType;
}