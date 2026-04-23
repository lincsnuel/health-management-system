package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.schedule.domain.model.enums.PatternType;
import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import jakarta.persistence.*;
import lombok.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ShiftPatternEmbeddable {

    @Enumerated(EnumType.STRING)
    private PatternType type;

    @ElementCollection
    @CollectionTable(
            name = "pattern_weekly",
            joinColumns = @JoinColumn(name = "assignment_id") // Explicitly link to StaffAssignment ID
    )
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "weekly_pattern")
    private Map<DayOfWeek, ShiftType> weeklyPattern;

    @ElementCollection
    @CollectionTable(
            name = "pattern_cycle",
            joinColumns = @JoinColumn(name = "assignment_id") // Explicitly link to StaffAssignment ID
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type")
    private List<ShiftType> cycle;

    private LocalDate cycleStartDate;
}