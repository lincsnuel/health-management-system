package com.healthcore.tenantservice.domain.model.vo;

import com.healthcore.tenantservice.domain.exception.InvalidOperationalSettingsException;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record OperationalSettings(
        Integer appointmentSlotDuration,
        String workingDays,
        String workingHoursStart,
        String workingHoursEnd,
        Boolean allowOverbooking
) {

    public OperationalSettings {
        appointmentSlotDuration = normalizeSlotDuration(appointmentSlotDuration);
        workingDays = normalizeWorkingDays(workingDays);
        workingHoursStart = normalizeTime(workingHoursStart, "workingHoursStart");
        workingHoursEnd = normalizeTime(workingHoursEnd, "workingHoursEnd");
        normalizeBoolean(allowOverbooking, "allowOverbooking");

        validateTimeRange(workingHoursStart, workingHoursEnd);
    }

    private static Integer normalizeSlotDuration(Integer value) {
        if (value == null) {
            throw new InvalidOperationalSettingsException("appointmentSlotDuration must not be null");
        }

        if (value <= 0) {
            throw new InvalidOperationalSettingsException("appointmentSlotDuration must be greater than 0");
        }

        if (value > 240) { // max 4 hours per slot
            throw new InvalidOperationalSettingsException("appointmentSlotDuration must not exceed 240 minutes");
        }

        return value;
    }

    private static String normalizeWorkingDays(String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidOperationalSettingsException("workingDays must not be blank");
        }

        Set<String> validDays = Set.of(
                "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
        );

        List<String> days = Arrays.stream(value.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .toList();

        for (String day : days) {
            if (!validDays.contains(day)) {
                throw new InvalidOperationalSettingsException("invalid working day: " + day);
            }
        }

        // remove duplicates and keep consistent ordering
        return days.stream()
                .distinct()
                .collect(Collectors.joining(","));
    }

    private static String normalizeTime(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidOperationalSettingsException(fieldName + " must not be blank");
        }

        try {
            LocalTime time = LocalTime.parse(value.trim());
            return time.toString(); // canonical format HH:mm:ss
        } catch (DateTimeParseException e) {
            throw new InvalidOperationalSettingsException(fieldName + " must be a valid time (HH:mm)");
        }
    }

    private static Boolean normalizeBoolean(Boolean value, String fieldName) {
        if (value == null) {
            throw new InvalidOperationalSettingsException(fieldName + " must not be null");
        }
        return value;
    }

    private static void validateTimeRange(String start, String end) {
        LocalTime startTime = LocalTime.parse(start);
        LocalTime endTime = LocalTime.parse(end);

        if (!endTime.isAfter(startTime)) {
            throw new InvalidOperationalSettingsException("workingHoursEnd must be after workingHoursStart");
        }
    }
}