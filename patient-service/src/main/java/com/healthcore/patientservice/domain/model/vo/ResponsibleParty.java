package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import java.util.Objects;

public class ResponsibleParty {

    private final PersonName name;
    private final PhoneNumber phoneNumber;
    private final String relationship;
    private final Address address;
    private final ResponsiblePartyType type;

    public ResponsibleParty(
            PersonName name,
            PhoneNumber phoneNumber,
            String relationship,
            Address address,
            ResponsiblePartyType type
    ) {
        this.name = Objects.requireNonNull(name, "Name is required");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number is required");
        this.relationship = Objects.requireNonNull(relationship, "Relationship is required");
        this.address = Objects.requireNonNull(address, "Address is required");
        this.type = Objects.requireNonNull(type, "Type is required");
    }

    /**
     * Reconstructs the Domain Model from flat persistence data.
     */
    public static ResponsibleParty of(
            PersonName name,
            PhoneNumber phone,
            String relationship,
            ResponsiblePartyType type,
            Address address) {
        return new ResponsibleParty(
                name,
                phone,
                relationship,
                address,
                type
        );
    }

    // Accessors for the Mapper
    public String firstName() {
        return this.name.firstName();
    }
    public String lastName() {
        return this.name.lastName();
    }
    public String fullName() { return name.getFullName(); }
    public String phoneNumber() { return phoneNumber.value(); }
    public String relationship() { return relationship; }
    public ResponsiblePartyType type() { return type; }
    public Address address() { return address; }
}