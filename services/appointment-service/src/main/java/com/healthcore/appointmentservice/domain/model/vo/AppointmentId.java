package com.healthcore.appointmentservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record AppointmentId(UUID value) {

    public AppointmentId {
        Objects.requireNonNull(value, "AppointmentId value cannot be null");
    }

    public static AppointmentId of(UUID value) {
        return new AppointmentId(value);
    }

    public static AppointmentId generate() {
        return new AppointmentId(UUID.randomUUID());
    }
}