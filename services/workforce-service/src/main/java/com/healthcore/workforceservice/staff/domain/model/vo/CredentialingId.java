package com.healthcore.workforceservice.staff.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record CredentialingId(UUID value) {

    public CredentialingId {
        Objects.requireNonNull(value, "Credential ID cannot be null");
    }

    public static CredentialingId of(UUID id) {
        return new CredentialingId(id);
    }

    public static CredentialingId newId() {return new CredentialingId(UUID.randomUUID());}
}