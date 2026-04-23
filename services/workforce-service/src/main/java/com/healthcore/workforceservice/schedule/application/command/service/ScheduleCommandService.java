package com.healthcore.workforceservice.schedule.application.command.service;

import com.healthcore.workforceservice.schedule.domain.model.schedule.*;
import com.healthcore.workforceservice.schedule.domain.repository.StaffScheduleRepository;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final StaffScheduleRepository repository;
    private final DomainEventPublisher publisher;

    // =========================
    // 1. DEFINE DEPARTMENT DEMAND
    // =========================

    public void defineDepartmentSchedule(
            DepartmentId departmentId,
            DayOfWeek day,
            List<SlotDemand> demands
    ) {

        Schedule schedule = getOrCreate(departmentId);

        schedule.getDepartment().define(
                new DepartmentDaySchedule(day, demands)
        );

        persist(schedule);
    }

    // =========================
    // 2. ASSIGN SHIFT PATTERN
    // =========================

    public void assignPattern(
            DepartmentId departmentId,
            StaffShiftPattern pattern
    ) {

        Schedule schedule = getExisting(departmentId);

        schedule.getStaff().addPattern(pattern);

        persist(schedule);
    }

    // =========================
    // 3. APPLY LEAVE
    // =========================

    public void applyLeave(
            DepartmentId departmentId,
            StaffLeave leave
    ) {

        Schedule schedule = getExisting(departmentId);

        schedule.getStaff().applyLeave(leave);

        persist(schedule);
    }

    // =========================
    // 4. CANCEL LEAVE
    // =========================

    public void cancelLeave(
            DepartmentId departmentId,
            StaffLeave leave
    ) {

        Schedule schedule = getExisting(departmentId);

        schedule.getStaff().cancelLeave(leave);

        persist(schedule);
    }

    // =========================
    // INTERNAL
    // =========================

    private void persist(Schedule schedule) {

        repository.save(schedule);

        if (!schedule.getDomainEvents().isEmpty()) {
            publisher.publish(schedule.getDomainEvents());
            schedule.clearDomainEvents();
        }
    }

    private Schedule getOrCreate(DepartmentId departmentId) {

        return repository.findByDepartmentId(departmentId)
                .orElseGet(() ->
                        new Schedule(
                                new DepartmentScheduleAggregate(),
                                new StaffAllocationAggregate()
                        )
                );
    }

    private Schedule getExisting(DepartmentId departmentId) {

        return repository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}