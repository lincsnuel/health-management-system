package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "staff_shift")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffShiftEntity {

    @Id
    private UUID id;

    private UUID staffId;

    @Enumerated(EnumType.STRING)
    private ShiftType shiftType;

    @Embedded
    private TimeSlotEmbeddable timeSlot;

    @Embedded
    private RecurrenceEmbeddable recurrence;

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