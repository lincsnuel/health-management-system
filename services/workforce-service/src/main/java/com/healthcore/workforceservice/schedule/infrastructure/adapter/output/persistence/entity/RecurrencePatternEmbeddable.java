package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecurrencePatternEmbeddable {

    @Column(name = "days_of_week")
    private String daysOfWeek; // e.g. "MONDAY,TUESDAY"

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    private RecurrencePatternEmbeddable(Set<DayOfWeek> daysOfWeek,
                                        LocalDate startDate,
                                        LocalDate endDate) {
        this.daysOfWeek = encode(daysOfWeek);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // =========================
    // HELPERS
    // =========================

    public Set<DayOfWeek> decodeDays() {
        if (daysOfWeek == null || daysOfWeek.isBlank()) return Set.of();

        return Arrays.stream(daysOfWeek.split(","))
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
    }

    private String encode(Set<DayOfWeek> days) {
        return days == null ? "" :
                String.join(",", days.stream().map(Enum::name).toList());
    }
}