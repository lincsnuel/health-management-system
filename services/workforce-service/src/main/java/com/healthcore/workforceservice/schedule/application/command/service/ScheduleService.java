package com.healthcore.workforceservice.schedule.application.command.service;

import com.healthcore.workforceservice.schedule.application.command.model.DepartmentScheduleRequest;
import com.healthcore.workforceservice.schedule.application.command.model.StaffLeaveRequest;
import com.healthcore.workforceservice.schedule.application.command.model.StaffShiftRequest;
import com.healthcore.workforceservice.schedule.application.command.usecase.ScheduleUseCase;
import com.healthcore.workforceservice.schedule.domain.model.enums.ScheduleStrategy;
import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.ScheduleId;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;

import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService implements ScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public void defineDepartmentSchedule(String departmentId, DepartmentScheduleRequest request) {

        Schedule schedule = getOrCreate(departmentId);

        schedule.defineDepartmentSchedule(
                request.getDay(),
                request.getSlotCapacities()
        );

        // CRITICAL: rebalance after structure change
        schedule.rebalance();

        persist(schedule);
    }

    @Override
    public void assignStaffShift(String departmentId, StaffId staffId, StaffShiftRequest request) {

        Schedule schedule = getOrCreate(departmentId);

        schedule.assignShift(
                staffId,
                request.getShiftType(),
                request.getSlot(),
                request.getRecurrence()
        );

        persist(schedule);
    }

    @Override
    public void applyLeave(String departmentId, StaffId staffId, StaffLeaveRequest request) {

        Schedule schedule = getOrCreate(departmentId);

        schedule.applyLeave(
                staffId,
                request.getStart(),
                request.getEnd(),
                request.getLeaveType()
        );

        schedule.rebalance();

        persist(schedule);
    }

    @Override
    public void removeShift(String departmentId, StaffId staffId, TimeSlot slot) {

        Schedule schedule = getSchedule(new DepartmentId(departmentId));

        schedule.removeShift(staffId, slot);

        schedule.rebalance();

        persist(schedule);
    }

    @Override
    public void cancelLeave(String departmentId, StaffId staffId, LocalDateTime start, LocalDateTime end) {

        Schedule schedule = getSchedule(new DepartmentId(departmentId));

        schedule.cancelLeave(staffId, start.toLocalDate(), end.toLocalDate());

        schedule.rebalance();

        persist(schedule);
    }

    @Override
    public void publishAvailability(String departmentId, LocalDate date) {

        Schedule schedule = getSchedule(new DepartmentId(departmentId));

        var event = schedule.calculateAndRaiseAvailability(date);

        persist(schedule);

        eventPublisher.publish(List.of(event));
    }

    // ---------------------

    private void persist(Schedule schedule) {
        scheduleRepository.save(schedule);
        eventPublisher.publish(schedule.getDomainEvents());
        schedule.clearDomainEvents();
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
}