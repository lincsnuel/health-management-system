package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "staff_shifts",
        indexes = {
                @Index(name = "idx_shift_staff", columnList = "staffId"),
                @Index(name = "idx_shift_allocation", columnList = "staff_allocation_id")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffShiftEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID staffId;

    private String shiftType;

    private LocalTime startTime;

    private LocalTime endTime;

    @Embedded
    private RecurrencePatternEmbeddable recurrence;

    @ManyToOne(fetch = FetchType.LAZY)
    private StaffAllocationEntity staffAllocation;

    @Builder
    private StaffShiftEntity(UUID staffId,
                             String shiftType,
                             LocalTime startTime,
                             LocalTime endTime,
                             RecurrencePatternEmbeddable recurrence) {
        this.staffId = staffId;
        this.shiftType = shiftType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.recurrence = recurrence;
    }

    // =========================
    // RELATIONSHIP WIRING
    // =========================

    public void assignToStaffAllocation(StaffAllocationEntity allocation) {
        this.staffAllocation = allocation;
    }
}