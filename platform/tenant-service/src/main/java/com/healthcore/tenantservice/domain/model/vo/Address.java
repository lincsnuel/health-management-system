package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidAddressException;

import java.util.Optional;

/**
 * Address Value Object.
 * Records are immutable by default and ideal for DDD Value Objects.
 */
public record Address(
        String street,
        String city,
        String state,
        String country,
        String postalCode
) {

    public Address {
        street = normalize(street, "Street");
        city = normalize(city, "City");
        state = normalize(state, "State");
        country = normalize(country, "Country");
        postalCode = normalize(postalCode, "Postal Code");
    }

    private static String normalize(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidAddressException(fieldName + " must not be blank");
        }
        return value.trim();
    }
}