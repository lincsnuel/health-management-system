package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidNextOfKinException;

public record NextOfKin(
        String fullName,
        String relationship,
        PhoneNumber contactNumber,
        String address // simplified to match entity
) {

    public NextOfKin {
        if (fullName == null || fullName.isBlank()) {
            throw new InvalidNextOfKinException("Next of kin must have a name");
        }
        if (relationship == null || relationship.isBlank()) {
            throw new InvalidNextOfKinException("Next of kin must have a relationship");
        }
        if (contactNumber == null) {
            throw new InvalidNextOfKinException("Next of kin must have a phone number");
        }

        // Normalize relationship string
        relationship = normalizeRelationship(relationship);
    }

    private static String normalizeRelationship(String relationship) {
        relationship = relationship.trim();
        return Character.toUpperCase(relationship.charAt(0)) + relationship.substring(1).toLowerCase();
    }

    public static NextOfKin of(String fullName, String relationship, PhoneNumber contactNumber, String address) {
        return new NextOfKin(fullName, relationship, contactNumber, address);
    }
}