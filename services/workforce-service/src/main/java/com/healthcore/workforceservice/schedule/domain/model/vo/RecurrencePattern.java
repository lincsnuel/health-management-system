package com.healthcore.workforceservice.schedule.domain.model.vo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

public record RecurrencePattern(
        Set<DayOfWeek> daysOfWeek,
        LocalDate startDate,
        LocalDate endDate
) {

    public RecurrencePattern {
        if (daysOfWeek == null || daysOfWeek.isEmpty()) {
            throw new IllegalArgumentException("Days of week required");
        }
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end date required");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Invalid recurrence range");
        }
    }

    public boolean appliesTo(LocalDate date) {
        return !date.isBefore(startDate)
                && !date.isAfter(endDate)
                && daysOfWeek.contains(date.getDayOfWeek());
    }

    // Needed for overlap detection
    public boolean overlaps(RecurrencePattern other) {
        boolean dateOverlap =
                !this.endDate.isBefore(other.startDate) &&
                        !other.endDate.isBefore(this.startDate);

        boolean dayOverlap =
                this.daysOfWeek.stream().anyMatch(other.daysOfWeek::contains);

        return dateOverlap && dayOverlap;
    }

    //HELPER
    public static RecurrencePattern weekly(DayOfWeek day) {
        return new RecurrencePattern(
                Set.of(day),
                LocalDate.now(),
                LocalDate.now().plusYears(10) // long-lived default
        );
    }
}