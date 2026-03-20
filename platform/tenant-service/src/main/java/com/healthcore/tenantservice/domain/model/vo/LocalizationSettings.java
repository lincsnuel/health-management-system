package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidLocalizationSettingsException;

import java.time.ZoneId;
import java.util.Currency;
import java.util.Locale;

public record LocalizationSettings(
        String timezone,
        String currency,
        String dateFormat,
        String language
) {

    public LocalizationSettings {
        timezone = normalizeTimezone(timezone);
        currency = normalizeCurrency(currency);
        dateFormat = normalizeDateFormat(dateFormat);
        language = normalizeLanguage(language);
    }

    private static String normalizeTimezone(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidLocalizationSettingsException("timezone must not be blank");
        }

        String normalized = value.trim();

        try {
            ZoneId zone = ZoneId.of(normalized);
            return zone.getId(); // canonical form
        } catch (Exception e) {
            throw new InvalidLocalizationSettingsException("invalid timezone");
        }
    }

    private static String normalizeCurrency(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidLocalizationSettingsException("currency must not be blank");
        }

        String normalized = value.trim().toUpperCase();

        try {
            Currency curr = Currency.getInstance(normalized);
            return curr.getCurrencyCode(); // canonical form
        } catch (Exception e) {
            throw new InvalidLocalizationSettingsException("invalid currency code");
        }
    }

    private static String normalizeDateFormat(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidLocalizationSettingsException("dateFormat must not be blank");
        }

        return value.trim();
    }

    private static String normalizeLanguage(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidLocalizationSettingsException("language must not be blank");
        }

        String normalized = value.trim().toLowerCase();

        boolean isValid = false;
        for (String lang : Locale.getISOLanguages()) {
            if (lang.equals(normalized)) {
                isValid = true;
                break;
            }
        }

        if (!isValid) {
            throw new InvalidLocalizationSettingsException("invalid language code");
        }

        return normalized;
    }
}