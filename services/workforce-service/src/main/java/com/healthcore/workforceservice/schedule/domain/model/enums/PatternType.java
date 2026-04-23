package com.healthcore.workforceservice.schedule.domain.model.enums;

public enum ShiftPatternType {
    WEEKLY_FIXED,     // e.g. Mon/Wed mornings
    TWO_TWO_TWO,      // 2 day, 2 night, 2 off
    CUSTOM_ROTATION   // fully flexible
}