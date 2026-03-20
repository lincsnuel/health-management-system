package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidContactInfoException;

public record ContactInfo(
        String phone,
        String email,
        String website
) {

    public ContactInfo {
        try {
            phone = normalizePhone(phone);
            email = normalizeEmail(email);
        } catch (InvalidContactInfoException e) {
            throw new InvalidContactInfoException(e);
        }
        website = normalizeOptional(website);
    }

    private static String normalizePhone(String value) throws InvalidContactInfoException {
        if (value == null || value.isBlank()) {
            throw new InvalidContactInfoException("phone must not be blank");
        }

        String normalized = value.trim().replaceAll("\\s+", "");

        // Basic international phone validation (+234..., etc.)
        if (!normalized.matches("^\\+?[0-9]{7,15}$")) {
            throw new InvalidContactInfoException("phone must be a valid number");
        }

        return normalized;
    }

    private static String normalizeEmail(String value) throws InvalidContactInfoException {
        if (value == null || value.isBlank()) {
            throw new InvalidContactInfoException("email must not be blank");
        }

        String normalized = value.trim().toLowerCase();

        if (!normalized.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new InvalidContactInfoException("email must be valid");
        }

        return normalized;
    }

    private static String normalizeOptional(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}