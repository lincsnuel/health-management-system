package com.healthcore.patientservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record PatientId(UUID value) {

    public PatientId {
        Objects.requireNonNull(value, "Patient ID cannot be null");
    }

    public static PatientId of(UUID id) {
        return new PatientId(id);
    }
}