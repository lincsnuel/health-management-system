package com.healthcore.workforceservice.schedule.domain.model.schedule;

import com.healthcore.workforceservice.schedule.domain.event.schedule.*;
import com.healthcore.workforceservice.schedule.domain.model.enums.ScheduleStrategy;
import com.healthcore.workforceservice.schedule.domain.model.vo.*;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.event.DomainEvent;

import lombok.Getter;

import java.time.*;
import java.util.*;

@Getter
public class Schedule {

    private final ScheduleId scheduleId;
    private final DepartmentId departmentId;
    private final ScheduleStrategy strategy;

    private final DepartmentScheduleAggregate departmentSchedules;
    private final StaffAllocationAggregate staffAllocations;

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    private Schedule(ScheduleId id,
                     DepartmentId departmentId,
                     ScheduleStrategy strategy) {
        this.scheduleId = id;
        this.departmentId = departmentId;
        this.strategy = strategy;

        // Pass event registrar (important)
        this.departmentSchedules = new DepartmentScheduleAggregate(departmentId, this::registerEvent);
        this.staffAllocations = new StaffAllocationAggregate(this::registerEvent);
    }

    public static Schedule create(ScheduleId id,
                                  DepartmentId departmentId,
                                  ScheduleStrategy strategy) {
        Schedule schedule = new Schedule(id, departmentId, strategy);

        schedule.registerEvent(new ScheduleCreatedEvent(
                id.value(),
                departmentId.value(),
                LocalDateTime.now()
        ));

        return schedule;
    }

    public static Schedule reconstruct(
            ScheduleId id,
            DepartmentId departmentId,
            ScheduleStrategy strategy,
            DepartmentScheduleAggregate departmentSchedules,
            StaffAllocationAggregate staffAllocations
    ) {
        Schedule schedule = new Schedule(id, departmentId, strategy);

        // overwrite freshly-created aggregates with persisted ones
        schedule.departmentSchedules.clearAndReplace(departmentSchedules);
        schedule.staffAllocations.clearAndReplace(staffAllocations);

        return schedule;
    }

    // =========================
    // BEHAVIOR (ROOT-OWNED)
    // =========================

    public void defineDepartmentSchedule(DayOfWeek day,
                                         Map<TimeSlot, Integer> slotCapacities) {

        departmentSchedules.defineSchedule(day, slotCapacities);

    }


    // =========================
    // EVENT MANAGEMENT
    // =========================

    private void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}