package com.healthcore.workforceservice.schedule.infrastructure.config;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;

public final class ShiftWorkloadPolicy {

    public static double weightOf(ShiftType shift) {

        return switch (shift) {
            case MORNING -> 1.0;
            case AFTERNOON -> 1.2;
            case NIGHT -> 1.8;
            case FULL_DAY -> 2.0;
            case OFF -> 0.0;
        };
    }

    private ShiftWorkloadPolicy() {
        // prevent instantiation
    }
}