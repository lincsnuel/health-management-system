package com.healthcore.workforceservice.schedule.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record ScheduleId(UUID value) {

    public ScheduleId {
        Objects.requireNonNull(value, "Patient ID cannot be null");
    }

    public static ScheduleId of(UUID id) {
        return new ScheduleId(id);
    }
}