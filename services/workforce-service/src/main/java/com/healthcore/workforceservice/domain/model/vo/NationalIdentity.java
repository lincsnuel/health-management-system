package com.healthcore.workforceservice.domain.model.vo;

import com.healthcore.workforceservice.domain.exception.DomainException;
import com.healthcore.workforceservice.domain.model.enums.IdentityType;

import java.util.Objects;

/**
 * Validated Identity Value Object (NIN, BVN, etc.)
 */
public record NationalIdentity(IdentityType type, String number) {
    public NationalIdentity {
        Objects.requireNonNull(type, "Identity type is required");
        Objects.requireNonNull(number, "Identity number is required");
        number = number.trim().toUpperCase();
        validate(type, number);
    }

    private void validate(IdentityType type, String number) {
        switch (type) {
            case NIN, BVN -> {
                if (!number.matches("\\d{11}"))
                    throw new DomainException(type + " must be exactly 11 digits");
            }
            case INTERNATIONAL_PASSPORT -> {
                if (!number.matches("[A-Z0-9]{6,15}"))
                    throw new DomainException("Invalid passport format");
            }
            default -> {
                if (number.length() < 5) throw new DomainException("Invalid identity number length");
            }
        }
    }
}