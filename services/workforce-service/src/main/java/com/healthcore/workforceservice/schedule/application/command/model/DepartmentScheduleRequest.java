package com.healthcore.workforceservice.schedule.application.command.model;

import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import lombok.Getter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Getter
public class DepartmentScheduleRequest {

    private DayOfWeek day;
    private List<TimeSlot> slots;
    private Map<TimeSlot, Integer> slotCapacities;
}