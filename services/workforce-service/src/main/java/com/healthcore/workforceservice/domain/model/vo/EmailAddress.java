package com.healthcore.workforceservice.domain.model.vo;

import com.healthcore.workforceservice.domain.exception.InvalidEmailException;

public record EmailAddress(String value) {

    public EmailAddress {
        if (value == null) {
            throw new InvalidEmailException("Email cannot be null");
        }

        value = normalize(value);

        if (value.isBlank()) {
            throw new InvalidEmailException("Email cannot be blank");
        }

        if (!isValidFormat(value)) {
            throw new InvalidEmailException("Invalid email format: " + value);
        }

        if (!isAllowedDomain(value)) {
            throw new InvalidEmailException("Email domain is not allowed: " + value);
        }
    }

    // ================== VALIDATION ==================

    private static String normalize(String email) {
        return email.trim().toLowerCase();
    }

    private static boolean isValidFormat(String email) {
        return email.contains("@") && email.indexOf("@") > 0;
    }

    /**
     * Domain-level restriction hook.
     * Currently, allows all domains.
     * Can later restrict to hospital-approved domains.
     */
    private static boolean isAllowedDomain(String email) {
        return true;
    }

    public static EmailAddress of(String email) {
        return new EmailAddress(email);
    }

    /**
     * Masks email for logging:
     * john.doe@example.com -> j***@example.com
     * a@example.com -> ***@example.com
     */
    public String masked() {
        int atIndex = value.indexOf("@");

        if (atIndex <= 1) {
            return "***" + value.substring(atIndex);
        }

        return value.charAt(0) + "***" + value.substring(atIndex);
    }
}