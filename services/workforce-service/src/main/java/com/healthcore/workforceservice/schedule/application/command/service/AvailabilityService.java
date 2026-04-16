package com.healthcore.workforceservice.schedule.application.command.service;

import com.healthcore.workforceservice.schedule.application.command.usecase.AvailabilityUseCase;
import com.healthcore.workforceservice.schedule.domain.event.schedule.DepartmentAvailabilityCalculatedEvent;
import com.healthcore.workforceservice.schedule.domain.model.vo.DepartmentAvailability;
import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.model.vo.SlotCapacity;
import com.healthcore.workforceservice.schedule.domain.model.vo.TimeSlot;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
import com.healthcore.workforceservice.schedule.domain.service.AvailabilityPolicyDomainService;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AvailabilityService implements AvailabilityUseCase {

    private final AvailabilityPolicyDomainService policyService;
    private final ScheduleRepository scheduleRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public DepartmentAvailability compute(DepartmentId departmentId, LocalDate date) {

        Schedule schedule = getSchedule(departmentId);

        DepartmentAvailability base = schedule.calculateAvailability(date);

        return policyService.applyPolicy(
                base,
                2,      // min staff threshold
                1.2                 // overbooking factor
        );
    }

    public Map<TimeSlot, Integer> computeAsMap(DepartmentId departmentId, LocalDate date) {

        DepartmentAvailability availability = compute(departmentId, date);

        return availability.slotCapacities().stream()
                .collect(Collectors.toMap(
                        SlotCapacity::slot,
                        SlotCapacity::assignedStaff
                ));
    }

    @Override
    @Transactional
    public void publish(DepartmentId departmentId, LocalDate date) {

        Schedule schedule = getSchedule(departmentId);

        // 1. DOMAIN COMPUTATION
        DepartmentAvailabilityCalculatedEvent event =
                schedule.calculateAndRaiseAvailability(date);

        // 2. PERSIST (if needed for audit or state versioning)
        scheduleRepository.save(schedule);

        // 3. PUBLISH (OUTBOX WILL PICK THIS UP IN REAL SYSTEM)
        eventPublisher.publish(List.of(event));
    }

    private Schedule getSchedule(DepartmentId departmentId) {
        return scheduleRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
}