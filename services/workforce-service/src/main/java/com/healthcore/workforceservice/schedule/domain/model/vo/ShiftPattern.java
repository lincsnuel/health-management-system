package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.PatternType;
import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class ShiftPattern {

    private final PatternType type;

    // For FIXED_WEEKLY
    private final Map<DayOfWeek, ShiftType> weeklyPattern;

    // For CYCLIC (e.g. 2-2-2)
    private final List<ShiftType> cycle;

    // Anchor date for cyclic patterns
    private final LocalDate cycleStartDate;

    public ShiftPattern(
            PatternType type,
            Map<DayOfWeek, ShiftType> weeklyPattern,
            List<ShiftType> cycle,
            LocalDate cycleStartDate
    ) {
        this.type = type;
        this.weeklyPattern = weeklyPattern;
        this.cycle = cycle;
        this.cycleStartDate = cycleStartDate;
    }

    public ShiftType resolve(LocalDate date) {

        return switch (type) {

            case FIXED_WEEKLY -> weeklyPattern.getOrDefault(
                    date.getDayOfWeek(),
                    ShiftType.OFF
            );

            case CYCLIC -> {
                long days = ChronoUnit.DAYS.between(cycleStartDate, date);
                int index = (int) (days % cycle.size());
                yield cycle.get(index);
            }

            case CUSTOM -> throw new UnsupportedOperationException(
                    "Custom pattern requires explicit handling"
            );
        };
    }
}