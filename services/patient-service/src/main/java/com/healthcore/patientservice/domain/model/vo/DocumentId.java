package com.healthcore.patientservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record DocumentId(UUID value) {

    public DocumentId {
        Objects.requireNonNull(value, "Document ID cannot be null");
    }

    public static DocumentId of(UUID id) {
        return new DocumentId(id);
    }

    public static DocumentId newId() {
        return new DocumentId(UUID.randomUUID());
    }
}