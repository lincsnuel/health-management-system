package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidNotificationSettingsException;

public record NotificationSettings(
        Boolean smsEnabled,
        Boolean emailEnabled,
        Integer appointmentReminderHours
) {

    public NotificationSettings {
        normalizeBoolean(smsEnabled, "smsEnabled");
        normalizeBoolean(emailEnabled, "emailEnabled");
        appointmentReminderHours = normalizeReminderHours(appointmentReminderHours);
    }

    private static Boolean normalizeBoolean(Boolean value, String fieldName) {
        if (value == null) {
            throw new InvalidNotificationSettingsException(fieldName + " must not be null");
        }
        return value;
    }

    private static Integer normalizeReminderHours(Integer value) {
        if (value == null) {
            throw new InvalidNotificationSettingsException("appointmentReminderHours must not be null");
        }

        if (value < 0) {
            throw new InvalidNotificationSettingsException("appointmentReminderHours must not be negative");
        }

        if (value > 168) { // max 7 days
            throw new InvalidNotificationSettingsException("appointmentReminderHours must not exceed 168 hours");
        }

        return value;
    }
}