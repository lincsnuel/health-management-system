package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidNationalIdentityException;
import com.healthcore.patientservice.domain.model.enums.IdentityType;

public record NationalIdentity(
        IdentityType type,
        String number
) {

    public NationalIdentity {
        if (type == null) {
            throw new InvalidNationalIdentityException("Identity type is required");
        }

        number = normalize(number);

        validate(type, number);
    }

    private static String normalize(String number) {
        return number.trim().toUpperCase();
    }

    private static void validate(IdentityType type, String number) {

        switch (type) {

            case NIN, BVN -> {
                if (!number.matches("\\d{11}")) {
                    throw new InvalidNationalIdentityException(
                            type + " must be exactly 11 digits"
                    );
                }
            }

            case INTERNATIONAL_PASSPORT -> {
                if (!number.matches("[A-Z0-9]{6,15}")) {
                    throw new InvalidNationalIdentityException(
                            "Invalid passport number format"
                    );
                }
            }

            case DRIVERS_LICENSE -> {
                if (number.length() < 5) {
                    throw new InvalidNationalIdentityException(
                            "Invalid driver's license number"
                    );
                }
            }

            case VOTER_CARD -> {
                if (number.length() < 5) {
                    throw new InvalidNationalIdentityException(
                            "Invalid voter card number"
                    );
                }
            }
        }
    }

    public static NationalIdentity of(IdentityType type, String number) {
        return new NationalIdentity(type, number);
    }
}