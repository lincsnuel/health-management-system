package com.healthcore.workforceservice.schedule.application.listener;

import com.healthcore.workforceservice.schedule.domain.model.schedule.Schedule;
import com.healthcore.workforceservice.schedule.domain.repository.ScheduleRepository;
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(StaffActivatedEvent event) {

        DepartmentId departmentId = DepartmentId.of(event.getDepartmentId());
        StaffId staffId = StaffId.of(event.getStaffId());

        Schedule schedule = scheduleRepository
                .findByDepartmentId(departmentId)
                .orElse(null);

        if (schedule == null) {
            return; // No department schedule defined yet
        }

        schedule.onboardStaff(staffId);

        schedule.rebalance();

        scheduleRepository.save(schedule);
    }
}