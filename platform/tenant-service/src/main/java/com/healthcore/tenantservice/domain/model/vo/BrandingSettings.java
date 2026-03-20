package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidBrandingSettingsException;

public record BrandingSettings(
        String logoUrl,
        String primaryColor,
        String secondaryColor,
        String theme
) {

    public BrandingSettings {
        logoUrl = normalizeOptional(logoUrl);
        primaryColor = normalizeColor(primaryColor, "primaryColor");
        secondaryColor = normalizeColor(secondaryColor, "secondaryColor");
        theme = normalizeTheme(theme);
    }

    private static String normalizeOptional(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private static String normalizeColor(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidBrandingSettingsException(fieldName + " must not be blank");
        }

        String normalized = value.trim().toUpperCase();

        // Basic HEX color validation (#FFFFFF or #FFF)
        if (!normalized.matches("^#([A-F0-9]{6}|[A-F0-9]{3})$")) {
            throw new InvalidBrandingSettingsException(fieldName + " must be a valid HEX color");
        }

        return normalized;
    }

    private static String normalizeTheme(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidBrandingSettingsException("theme must not be blank");
        }

        String normalized = value.trim().toLowerCase();

        if (!normalized.equals("light") && !normalized.equals("dark")) {
            throw new InvalidBrandingSettingsException("theme must be either 'light' or 'dark'");
        }

        return normalized;
    }
}