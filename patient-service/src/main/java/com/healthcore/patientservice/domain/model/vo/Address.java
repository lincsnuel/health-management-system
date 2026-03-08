package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidAddressException;

public record Address(
        String street,
        String city,
        String state,
        String country,
        boolean primary
) {

    public Address {
        street = normalize(street);
        city = normalize(city);
        state = normalize(state);
        country = normalize(country);
    }

    public static Address of(
            String street,
            String city,
            String state,
            String country,
            boolean primary
    ) {
        return new Address(street, city, state, country, primary);
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAddressException("Field must not be blank");
        }
        return value.trim();
    }

    public boolean isPrimary() {
        return primary;
    }

    public Address unsetPrimary() {
        return new Address(street, city, state, country, false);
    }

    public Address setPrimary() {
        return new Address(street, city, state, country, true);
    }
}