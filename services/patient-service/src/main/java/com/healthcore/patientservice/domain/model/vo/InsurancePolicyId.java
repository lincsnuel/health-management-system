package com.healthcore.patientservice.domain.model.vo;

import java.util.Objects;
import java.util.UUID;

public record InsurancePolicyId(UUID value) {

    public InsurancePolicyId {
        Objects.requireNonNull(value, "InsurancePolicyId cannot be null");
    }

    public static InsurancePolicyId of(UUID value) {
        return new InsurancePolicyId(value);
    }
    public static InsurancePolicyId newId() {
        return new InsurancePolicyId(UUID.randomUUID());
    }
}