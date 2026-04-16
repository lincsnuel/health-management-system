package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurrenceEmbeddable {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "recurrence_day",
            joinColumns = @JoinColumn(name = "shift_id") // important for ownership
    )
    @Column(name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    private LocalDate startDate;
    private LocalDate endDate;
}