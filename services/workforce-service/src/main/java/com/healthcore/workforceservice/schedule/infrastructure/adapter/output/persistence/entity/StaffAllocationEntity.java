package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "staff_allocation",
        indexes = {
                @Index(name = "idx_staff_alloc_schedule", columnList = "schedule_id")
        })
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StaffAllocationEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @OneToMany(mappedBy = "staffAllocation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<StaffShiftEntity> shifts = new ArrayList<>();

    @OneToMany(mappedBy = "staffAllocation",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<StaffLeaveEntity> leaves = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "staff_onboarded",
            joinColumns = @JoinColumn(name = "staff_allocation_id"))
    @Column(name = "staff_id")
    private Set<UUID> onboardedStaff = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "staff_workload",
            joinColumns = @JoinColumn(name = "staff_allocation_id"))
    @MapKeyColumn(name = "staff_id")
    @Column(name = "workload")
    private Map<UUID, Integer> workload = new HashMap<>();

    // =========================
    // RELATIONSHIP WIRING
    // =========================

    public void assignToSchedule(ScheduleEntity schedule) {
        this.schedule = schedule;
    }

    public void addShift(StaffShiftEntity shift) {
        shifts.add(shift);
        shift.assignToStaffAllocation(this);
    }

    public void addLeave(StaffLeaveEntity leave) {
        leaves.add(leave);
        leave.assignToStaffAllocation(this);
    }
}