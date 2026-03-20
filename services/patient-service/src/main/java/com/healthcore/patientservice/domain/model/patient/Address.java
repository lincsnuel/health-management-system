package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.exception.InvalidAddressException;
import com.healthcore.patientservice.domain.model.vo.AddressId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Address {

    private final AddressId addressId;
    private final String street;
    private final String city;
    private final String state;
    private final String country;
    private final boolean primary;

    private Address(
            AddressId addressId,
            String street,
            String city,
            String state,
            String country,
            boolean primary
    ) {
        this.addressId = Objects.requireNonNull(addressId);
        this.street = normalize(street);
        this.city = normalize(city);
        this.state = normalize(state);
        this.country = normalize(country);
        this.primary = primary;
    }

    /**
     * Factory for NEW Address (generates AddressId)
     */
    public static Address create(
            String street,
            String city,
            String state,
            String country,
            boolean primary
    ) {
        return new Address(
                AddressId.newId(),
                street,
                city,
                state,
                country,
                primary
        );
    }

    /**
     * Reconstruct EXISTING Address from persistence
     */
    public static Address reconstruct(
            AddressId addressId,
            String street,
            String city,
            String state,
            String country,
            boolean primary
    ) {
        return new Address(
                addressId,
                street,
                city,
                state,
                country,
                primary
        );
    }

    private static String normalize(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidAddressException("Field must not be blank");
        }
        return value.trim();
    }

    public Address unsetPrimary() {
        return new Address(
                this.addressId,
                this.street,
                this.city,
                this.state,
                this.country,
                false
        );
    }

    public Address setPrimary() {
        return new Address(
                this.addressId,
                this.street,
                this.city,
                this.state,
                this.country,
                true
        );
    }
}