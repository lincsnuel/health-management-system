package com.healthcore.tenantservice.domain.model.vo;

import org.jspecify.annotations.NullMarked;

import java.util.regex.Pattern;

public record Subdomain(String value) {

    private static final int MAX_LENGTH = 63;
    private static final Pattern VALID_PATTERN =
            Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");

    // Canonical constructor validation
    public Subdomain {
        validate(value);
    }

    public static Subdomain of(String value) {
        return new Subdomain(value);
    }

    private static void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Subdomain cannot be blank");
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Subdomain length must not exceed " + MAX_LENGTH
            );
        }

        if (!VALID_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                    "Invalid subdomain format. Must be lowercase alphanumeric with hyphens"
            );
        }
    }

    @Override
    @NullMarked
    public String toString() {
        return value;
    }
}