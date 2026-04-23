package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class ScheduleMatrix {

    private final Map<StaffId, Map<LocalDate, ShiftType>> matrix;

    public ScheduleMatrix(Map<StaffId, Map<LocalDate, ShiftType>> matrix) {
        this.matrix = matrix;
    }

    public ShiftType get(StaffId staffId, LocalDate date) {
        return matrix
                .getOrDefault(staffId, Map.of())
                .getOrDefault(date, ShiftType.OFF);
    }

    public Map<StaffId, Map<LocalDate, ShiftType>> asMap() {
        return Collections.unmodifiableMap(matrix);
    }
}