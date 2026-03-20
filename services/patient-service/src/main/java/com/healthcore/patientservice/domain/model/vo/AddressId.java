package com.healthcore.patientservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record AddressId(UUID value) {

    public AddressId {
        Objects.requireNonNull(value, "Address ID cannot be null");
    }

    public static AddressId of(UUID id) {
        return new AddressId(id);
    }

    public static AddressId newId() {
        return new AddressId(UUID.randomUUID());
    }
}
