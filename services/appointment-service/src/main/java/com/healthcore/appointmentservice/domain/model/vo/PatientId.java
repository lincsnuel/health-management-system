package com.healthcore.appointmentservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record PatientId(UUID value) {
    public PatientId {
        Objects.requireNonNull(value);
    }
}
