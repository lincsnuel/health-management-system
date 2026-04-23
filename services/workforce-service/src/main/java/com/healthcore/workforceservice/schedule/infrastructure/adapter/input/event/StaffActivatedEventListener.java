package com.healthcore.workforceservice.schedule.infrastructure.adapter.input.event;

import com.healthcore.workforceservice.schedule.application.eventhandler.StaffActivatedEventHandler;

import com.healthcore.workforceservice.schedule.domain.model.enums.StaffRole;
import com.healthcore.workforceservice.schedule.domain.model.vo.StaffRecord;
import com.healthcore.workforceservice.staff.domain.event.staff.StaffActivatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StaffActivatedEventListener {

    private final StaffActivatedEventHandler handler;

    @RabbitListener(queues = "staff.queue")
    public void consume(StaffActivatedEvent event) {

        // 1. Create local projection
        StaffRecord record = new StaffRecord(
                event.getStaffId(),
                event.getDepartmentId(),
                StaffRole.valueOf(event.getRole())
        );

        handler.on(record);
    }
}