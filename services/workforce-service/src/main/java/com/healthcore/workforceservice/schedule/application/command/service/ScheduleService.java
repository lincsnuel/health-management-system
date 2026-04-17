package com.healthcore.workforceservice.schedule.application.command.service;

import com.healthcore.workforceservice.schedule.application.command.model.DepartmentScheduleRequest;
import com.healthcore.workforceservice.schedule.application.command.model.StaffLeaveRequest;
import com.healthcore.workforceservice.schedule.application.command.model.StaffShiftRequest;
import com.healthcore.workforceservice.schedule.application.command.usecase.ScheduleUseCase;
import com.healthcore.workforceservice.schedule.domain.event.schedule.DepartmentAvailabilityCalculatedEvent;
import com.healthcore.workforceservice.schedule.domain.model.enums.ScheduleStrategy;
import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffLeave;
import com.healthcore.workforceservice.schedule.domain.model.schedule.StaffShift;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.vo.ScheduleId;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
import com.healthcore.workforceservice.schedule.domain.service.SchedulingEngine;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService implements ScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final SchedulingEngine schedulingEngine;
    private final DomainEventPublisher eventPublisher;

    // =========================
    // COMMANDS
    // =========================

    @Override
    public void defineDepartmentSchedule(String departmentId, DepartmentScheduleRequest request) {

        Schedule schedule = getOrCreate(departmentId);

        // Use aggregate root behavior
        schedule.defineDepartmentSchedule(
                request.getDay(),
                request.getSlotCapacities()
        );

        // Re-run assignment via engine
        schedulingEngine.autoAssign(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations()
        );

        persist(schedule);
    }

    @Override
    public void assignStaffShift(String departmentId, StaffId staffId, StaffShiftRequest request) {

        Schedule schedule = getOrCreate(departmentId);

        schedule.getStaffAllocations().assignShift(
                new StaffShift(
                        UUID.randomUUID(),
                        staffId,
                        request.getShiftType(),
                        request.getSlot(),
                        request.getRecurrence()
                )
        );

        persist(schedule);
    }

    @Override
    public void applyLeave(String departmentId, StaffId staffId, StaffLeaveRequest request) {

        Schedule schedule = getOrCreate(departmentId);

        schedule.getStaffAllocations().applyLeave(
                new StaffLeave(
                        UUID.randomUUID(),
                        staffId,
                        request.getStart(),
                        request.getEnd(),
                        request.getLeaveType()
                )
        );

        // Rebalance via engine
        schedulingEngine.autoAssign(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations()
        );

        persist(schedule);
    }

    @Override
    public void removeShift(String departmentId, StaffId staffId, TimeSlot slot) {

        Schedule schedule = getSchedule(new DepartmentId(departmentId));

        schedule.getStaffAllocations().removeShift(staffId, slot);

        schedulingEngine.autoAssign(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations()
        );

        persist(schedule);
    }

    @Override
    public void cancelLeave(String departmentId,
                            StaffId staffId,
                            LocalDateTime start,
                            LocalDateTime end) {

        Schedule schedule = getSchedule(new DepartmentId(departmentId));

        schedule.getStaffAllocations().cancelLeave(
                staffId,
                start.toLocalDate(),
                end.toLocalDate()
        );

        schedulingEngine.autoAssign(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations()
        );

        persist(schedule);
    }

    @Override
    public void publishAvailability(String departmentId, LocalDate date) {

        Schedule schedule = getSchedule(new DepartmentId(departmentId));

        DepartmentAvailability availability = schedulingEngine.calculateAvailability(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations(),
                schedule.getDepartmentId(),
                date
        );

        // Persist first (flush any domain events if present)
        persist(schedule);

        // Then publish derived event separately (NOT part of aggregate lifecycle)
        DepartmentAvailabilityCalculatedEvent event =
                new DepartmentAvailabilityCalculatedEvent(
                        departmentId,
                        date,
                        mapToShiftType(availability),
                        LocalDateTime.now()
                );

        eventPublisher.publish(List.of(event));
    }

    // =========================
    // INFRA HELPERS
    // =========================

    private void persist(Schedule schedule) {
        scheduleRepository.save(schedule);

        if (!schedule.getDomainEvents().isEmpty()) {
            eventPublisher.publish(schedule.getDomainEvents());
            schedule.clearDomainEvents();
        }
    }

    private Schedule getOrCreate(String departmentId) {
        return scheduleRepository.findByDepartmentId(new DepartmentId(departmentId))
                .orElseGet(() -> Schedule.create(
                        new ScheduleId(UUID.randomUUID()),
                        new DepartmentId(departmentId),
                        ScheduleStrategy.HYBRID
                ));
    }

    private Schedule getSchedule(DepartmentId departmentId) {
        return scheduleRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    private Map<ShiftType, Integer> mapToShiftType(DepartmentAvailability availability) {

        Map<ShiftType, Integer> result = new EnumMap<>(ShiftType.class);

        availability.slotCapacities().forEach(sc -> {
            ShiftType type = sc.slot().start().isBefore(LocalTime.NOON)
                    ? ShiftType.MORNING
                    : ShiftType.AFTERNOON;

            result.merge(type, sc.assignedStaff(), Integer::sum);
        });

        return result;
    }
}