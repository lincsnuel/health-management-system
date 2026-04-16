package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule;

import com.healthcore.workforceservice.schedule.domain.model.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "staff_leave")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaffLeaveEntity {

    @Id
    private UUID id;

    private UUID staffId;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private LeaveType leaveType;

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