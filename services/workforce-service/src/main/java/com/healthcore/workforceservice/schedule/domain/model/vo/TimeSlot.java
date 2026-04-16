package com.healthcore.workforceservice.schedule.domain.model.vo;

import java.time.LocalTime;

public record TimeSlot(LocalTime start, LocalTime end) {

    public TimeSlot {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Invalid time range");
        }
    }

    public boolean overlaps(TimeSlot other) {
        return !(other.end.isBefore(this.start) || other.start.isAfter(this.end));
    }

    public boolean contains(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }
}