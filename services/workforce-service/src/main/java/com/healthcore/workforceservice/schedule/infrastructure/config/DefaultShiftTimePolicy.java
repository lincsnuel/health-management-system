package com.healthcore.workforceservice.schedule.domain.service;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;

import java.time.LocalTime;

public class DefaultShiftTimePolicy implements ShiftTimePolicy {

    @Override
    public TimeSlot resolve(ShiftType type) {

        return switch (type) {

            case MORNING -> new TimeSlot(
                    LocalTime.of(7, 0),
                    LocalTime.of(15, 0)
            );

            case AFTERNOON -> new TimeSlot(
                    LocalTime.of(15, 0),
                    LocalTime.of(23, 0)
            );

            case NIGHT -> new TimeSlot(
                    LocalTime.of(23, 0),
                    LocalTime.of(7, 0)
            );

            case FULL_DAY -> new TimeSlot(
                    LocalTime.of(0, 0),
                    LocalTime.of(23, 59)
            );

            case OFF -> new TimeSlot(
                    LocalTime.MIDNIGHT,
                    LocalTime.MIDNIGHT
            );
        };
    }
}