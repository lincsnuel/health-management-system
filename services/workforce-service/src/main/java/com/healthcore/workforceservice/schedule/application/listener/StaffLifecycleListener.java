package com.healthcore.workforceservice.schedule.application.listener;

import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
import com.healthcore.workforceservice.schedule.domain.service.SchedulingEngine;
import com.healthcore.workforceservice.shared.domain.vo.DepartmentId;
import com.healthcore.workforceservice.shared.domain.vo.StaffId;
import com.healthcore.workforceservice.staff.domain.event.staff.StaffActivatedEvent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class StaffLifecycleListener {

    private final ScheduleRepository scheduleRepository;
    private final SchedulingEngine schedulingEngine;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StaffActivatedEvent event) {

        DepartmentId departmentId = DepartmentId.of(event.getDepartmentId());
        StaffId staffId = StaffId.of(event.getStaffId());

        Schedule schedule = scheduleRepository
                .findByDepartmentId(departmentId)
                .orElse(null);

        if (schedule == null) {
            return; // No schedule exists yet
        }

        // 1. Onboard staff into allocation aggregate
        schedule.getStaffAllocations().onboard(staffId);

        // 2. Trigger intelligent assignment via engine
        schedulingEngine.autoAssign(
                schedule.getDepartmentSchedules(),
                schedule.getStaffAllocations()
        );

        // 3. Persist updated aggregate
        scheduleRepository.save(schedule);
    }
}