package com.healthcore.workforceservice.schedule.application.command.service;

import com.healthcore.workforceservice.schedule.application.command.model.AvailabilityCommand;
import com.healthcore.workforceservice.schedule.application.command.usecase.AvailabilityUseCase;
import com.healthcore.workforceservice.schedule.domain.event.schedule.DepartmentAvailabilityCalculatedEvent;
import com.healthcore.workforceservice.schedule.domain.model.enums.ShiftType;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.SlotCapacity;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
import com.healthcore.workforceservice.schedule.domain.service.AvailabilityPolicyDomainService;
import com.healthcore.workforceservice.schedule.domain.service.SchedulingEngine;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AvailabilityService implements AvailabilityUseCase {

    private final SchedulingEngine schedulingEngine;
    private final AvailabilityPolicyDomainService policyService;
    private final ScheduleRepository scheduleRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public DepartmentAvailability compute(AvailabilityCommand command) {

        Schedule schedule = getSchedule(new DepartmentId(command.departmentId()));

        DepartmentAvailability base = schedulingEngine.calculateAvailability(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations(),
                schedule.getDepartmentId(),
                command.date()
        );

        return policyService.applyPolicy(
                base,
                base.totalRequired(),      // min staff threshold
                command.overBookingFactor()     // overbooking factor
        );
    }

    public Map<TimeSlot, Integer> computeAsMap(AvailabilityCommand command) {

        return compute(command)
                .slotCapacities()
                .stream()
                .collect(Collectors.toMap(
                        SlotCapacity::slot,
                        SlotCapacity::assignedStaff
                ));
    }

    @Override
    public void publish(AvailabilityCommand command) {

        Schedule schedule = getSchedule(new DepartmentId(command.departmentId()));

        DepartmentAvailability availability = schedulingEngine.calculateAvailability(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations(),
                schedule.getDepartmentId(),
                command.date()
        );

        DepartmentAvailabilityCalculatedEvent event =
                new DepartmentAvailabilityCalculatedEvent(
                        command.departmentId(),
                        command.date(),
                        mapToShiftType(availability),
                        LocalDateTime.now()
                );

        scheduleRepository.save(schedule);

        eventPublisher.publish(List.of(event));
    }

    // ---------------------

    private Map<ShiftType, Integer> mapToShiftType(DepartmentAvailability availability) {

        Map<ShiftType, Integer> result = new EnumMap<>(ShiftType.class);

        availability.slotCapacities().forEach(sc -> {
            ShiftType type = schedulingEngine.resolveShiftType(sc.slot());
            result.merge(type, sc.assignedStaff(), Integer::sum);
        });

        return result;
    }

    private Schedule getSchedule(DepartmentId departmentId) {
        return scheduleRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}