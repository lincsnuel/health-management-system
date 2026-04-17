package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "department_slots")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepartmentSlotEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalTime startTime;
    private LocalTime endTime;

    private int requiredStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    private DepartmentScheduleEntity departmentSchedule;

    @Builder
    private DepartmentSlotEntity(LocalTime startTime,
                                 LocalTime endTime,
                                 int requiredStaff) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.requiredStaff = requiredStaff;
    }

    // =========================
    // RELATIONSHIP WIRING
    // =========================

    public void assignToDepartmentSchedule(DepartmentScheduleEntity schedule) {
        this.departmentSchedule = schedule;
    }
}