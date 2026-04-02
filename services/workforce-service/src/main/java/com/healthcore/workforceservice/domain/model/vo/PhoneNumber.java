package com.healthcore.workforceservice.domain.model.vo;

import com.healthcore.workforceservice.domain.exception.InvalidPhoneNumberException;

public record PhoneNumber(String value) {

    public PhoneNumber {
        // Step 1: normalize raw input
        String normalized = normalize(value);

        // Step 2: validate normalized number
        validate(normalized);

        // Step 3: assign normalized number to record field
        value = normalized;
    }

    public static PhoneNumber of(String rawNumber) {
        return new PhoneNumber(rawNumber);
    }

    // ================== NORMALIZATION ==================
    private static String normalize(String number) {
        if (number == null || number.isBlank()) {
            throw new InvalidPhoneNumberException("Phone number cannot be null or blank");
        }

        // Remove spaces, dashes, parentheses
        number = number.replaceAll("[\\s\\-()]", "");

        // Convert 0XXXXXXXXXX → +234XXXXXXXXXX
        if (number.matches("^0\\d{10}$")) {
            number = "+234" + number.substring(1);
        }

        // Must now be in +234XXXXXXXXXX format
        if (!number.matches("^\\+234\\d{10}$")) {
            throw new InvalidPhoneNumberException("Phone number must be Nigerian");
        }

        return number;
    }

    // ================== VALIDATION ==================
    private static void validate(String number) {
        if (!number.matches("^\\+234[7890]\\d{9}$")) {
            throw new InvalidPhoneNumberException("Invalid Nigerian phone number");
        }
    }
}