package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "staff_leaves",
        indexes = {
                @Index(name = "idx_leave_staff", columnList = "staffId")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StaffLeaveEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private UUID staffId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String leaveType;

    @ManyToOne(fetch = FetchType.LAZY)
    private StaffAllocationEntity staffAllocation;

    @Builder
    private StaffLeaveEntity(UUID staffId,
                             LocalDate startDate,
                             LocalDate endDate,
                             String leaveType) {
        this.staffId = staffId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
    }

    // =========================
    // RELATIONSHIP WIRING
    // =========================

    public void assignToStaffAllocation(StaffAllocationEntity allocation) {
        this.staffAllocation = allocation;
    }
}