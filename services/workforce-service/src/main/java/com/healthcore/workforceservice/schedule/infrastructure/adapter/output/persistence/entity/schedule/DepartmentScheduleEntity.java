package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "department_schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentScheduleEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ElementCollection
    @CollectionTable(name = "department_schedule_slot",
            joinColumns = @JoinColumn(name = "department_schedule_id"))
    private List<TimeSlotEmbeddable> slots = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    // package-private (no public setter)
    void attachTo(ScheduleEntity schedule) {
        this.schedule = schedule;
    }

    void detach() {
        this.schedule = null;
    }
}