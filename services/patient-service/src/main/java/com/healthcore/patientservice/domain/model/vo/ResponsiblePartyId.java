package com.healthcore.patientservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record ResponsiblePartyId(UUID value) {
    public ResponsiblePartyId {
        Objects.requireNonNull(value, "ID cannot be null");
    }

    public static ResponsiblePartyId of(UUID id) {
        return new ResponsiblePartyId(id);
    }

    public static ResponsiblePartyId newId() {
        return new ResponsiblePartyId(UUID.randomUUID());
    }
}
