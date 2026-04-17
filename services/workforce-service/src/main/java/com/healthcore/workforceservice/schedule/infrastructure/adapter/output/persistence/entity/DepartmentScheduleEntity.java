package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.util.*;

@Entity
@Table(name = "department_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepartmentScheduleEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String departmentId;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    private ScheduleEntity schedule;

    @OneToMany(mappedBy = "departmentSchedule",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final List<DepartmentSlotEntity> slots = new ArrayList<>();

    @Builder
    private DepartmentScheduleEntity(String departmentId,
                                     DayOfWeek dayOfWeek) {
        this.departmentId = departmentId;
        this.dayOfWeek = dayOfWeek;
    }

    // =========================
    // RELATIONSHIP WIRING
    // =========================

    public void assignToSchedule(ScheduleEntity schedule) {
        this.schedule = schedule;
    }

    public void addSlot(DepartmentSlotEntity slot) {
        slots.add(slot);
        slot.assignToDepartmentSchedule(this);
    }
}