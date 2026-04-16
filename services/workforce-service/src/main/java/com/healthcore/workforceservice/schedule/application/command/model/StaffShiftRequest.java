package com.healthcore.workforceservice.schedule.application.command.model;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.RecurrencePattern;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import lombok.Getter;

@Getter
public class StaffShiftRequest {

    private ShiftType shiftType;
    private TimeSlot slot;
    private RecurrencePattern recurrence;
}