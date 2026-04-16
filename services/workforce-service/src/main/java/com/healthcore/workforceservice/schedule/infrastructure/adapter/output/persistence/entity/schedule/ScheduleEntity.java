package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.ScheduleStrategy;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "schedule")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String departmentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleStrategy strategy;

    // =========================
    // CHILD COLLECTIONS
    // =========================

    @Builder.Default
    @OneToMany(
            mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DepartmentScheduleEntity> departmentSchedules = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StaffShiftEntity> staffShifts = new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "schedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StaffLeaveEntity> staffLeaves = new ArrayList<>();

// =========================
// RELATIONSHIP HELPERS
// =========================

    public void addDepartmentSchedule(DepartmentScheduleEntity entity) {
        entity.attachTo(this);
        this.departmentSchedules.add(entity);
    }

    public void clearDepartmentSchedules() {
        this.departmentSchedules.forEach(DepartmentScheduleEntity::detach);
        this.departmentSchedules.clear();
    }

    public void addStaffShift(StaffShiftEntity entity) {
        entity.attachTo(this);
        this.staffShifts.add(entity);
    }

    public void clearStaffShifts() {
        this.staffShifts.forEach(StaffShiftEntity::detach);
        this.staffShifts.clear();
    }

    public void addStaffLeave(StaffLeaveEntity entity) {
        entity.attachTo(this);
        this.staffLeaves.add(entity);
    }

    public void clearStaffLeaves() {
        this.staffLeaves.forEach(StaffLeaveEntity::detach);
        this.staffLeaves.clear();
    }
}