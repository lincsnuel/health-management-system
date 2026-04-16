package com.healthcore.workforceservice.staff.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record ProfessionalProfileId(UUID value) {

    public ProfessionalProfileId {
        Objects.requireNonNull(value, "ProfessionalProfile ID cannot be null");
    }

    public static ProfessionalProfileId of(UUID id) {
        return new ProfessionalProfileId(id);
    }

    public static ProfessionalProfileId newId() {return new ProfessionalProfileId(UUID.randomUUID());}
}