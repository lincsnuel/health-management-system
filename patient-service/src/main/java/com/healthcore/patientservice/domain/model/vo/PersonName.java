package com.healthcore.patientservice.domain.model.vo;

import com.healthcore.patientservice.domain.exception.InvalidPersonNameException;

public record PersonName(String firstName, String lastName) {

    // Canonical constructor: trims, validates, normalizes
    public PersonName {
        // Step 1: Trim whitespace
        firstName = firstName.trim();
        lastName = lastName.trim();

        // Step 2: Validate characters
        if (!firstName.matches("^[A-Za-z'-]+$")) {
            throw new InvalidPersonNameException("First name contains invalid characters");
        }
        if (!lastName.matches("^[A-Za-z'-]+$")) {
            throw new InvalidPersonNameException("Last name contains invalid characters");
        }

        // Step 3: Normalize (capitalize first letter, lowercase rest)
        firstName = normalize(firstName);
        lastName = normalize(lastName);
    }

    // ================== HELPER METHODS ==================
    private static String normalize(String name) {
        if (name.length() == 1) return name.toUpperCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase();
    }

    // Full name in "First Last" format
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Initials e.g., "John Doe" -> "J.D."
    public String getInitials() {
        return firstName.charAt(0) + "." + lastName.charAt(0) + ".";
    }

    // Convenience factory method
    public static PersonName of(String firstName, String lastName) {
        return new PersonName(firstName, lastName);
    }
}