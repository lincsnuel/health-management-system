package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import com.healthcore.workforceservice.schedule.domain.model.enums.ScheduleStrategy;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleEntity {

    @Id
    private UUID id;

    private String departmentId;

    @Enumerated(EnumType.STRING)
    private ScheduleStrategy strategy;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private DepartmentScheduleEntity departmentSchedule;

    @OneToOne(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private StaffAllocationEntity staffAllocation;

    @Transient
    private final List<Object> domainEvents = new ArrayList<>();

    @Builder
    private ScheduleEntity(UUID id,
                           String departmentId,
                           ScheduleStrategy strategy) {
        this.id = id;
        this.departmentId = departmentId;
        this.strategy = strategy;
    }

    // =========================
    // RELATIONSHIP WIRING
    // =========================

    public void assignDepartmentSchedule(DepartmentScheduleEntity schedule) {
        this.departmentSchedule = schedule;
        schedule.assignToSchedule(this);
    }

    public void assignStaffAllocation(StaffAllocationEntity allocation) {
        this.staffAllocation = allocation;
        allocation.assignToSchedule(this);
    }
}