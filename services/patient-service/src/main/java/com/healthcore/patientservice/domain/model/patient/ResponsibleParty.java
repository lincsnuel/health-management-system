package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import com.healthcore.patientservice.domain.model.vo.PersonName;
import com.healthcore.patientservice.domain.model.vo.PhoneNumber;
import com.healthcore.patientservice.domain.model.vo.ResponsiblePartyId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class ResponsibleParty {

    private final ResponsiblePartyId responsiblePartyId;
    private final PersonName name;
    private final PhoneNumber phoneNumber;
    private final String relationship;
    private final Address address;
    private final ResponsiblePartyType type;

    private ResponsibleParty(
            ResponsiblePartyId responsiblePartyId,
            PersonName name,
            PhoneNumber phoneNumber,
            String relationship,
            Address address,
            ResponsiblePartyType type
    ) {
        this.responsiblePartyId = Objects.requireNonNull(responsiblePartyId);
        this.name = Objects.requireNonNull(name, "Name is required");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number is required");
        this.relationship = normalizeRelationship(
                Objects.requireNonNull(relationship, "Relationship is required")
        );
        this.address = Objects.requireNonNull(address, "Address is required");
        this.type = Objects.requireNonNull(type, "Type is required");
    }

    private static String normalizeRelationship(String relationship) {
        relationship = relationship.trim();
        return Character.toUpperCase(relationship.charAt(0))
                + relationship.substring(1).toLowerCase();
    }

    /**
     * Factory for NEW ResponsibleParty (generate ID)
     */
    public static ResponsibleParty create(
            PersonName name,
            PhoneNumber phone,
            String relationship,
            ResponsiblePartyType type,
            Address address
    ) {
        return new ResponsibleParty(
                ResponsiblePartyId.newId(),
                name,
                phone,
                relationship,
                address,
                type
        );
    }

    /**
     * Reconstruct EXISTING ResponsibleParty from persistence
     */
    public static ResponsibleParty reconstruct(
            ResponsiblePartyId id,
            PersonName name,
            PhoneNumber phone,
            String relationship,
            ResponsiblePartyType type,
            Address address
    ) {
        return new ResponsibleParty(
                id,
                name,
                phone,
                relationship,
                address,
                type
        );
    }

}