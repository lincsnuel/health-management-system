package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.messaging;

import com.healthcore.workforceservice.shared.outbox.messaging.TopicResolver;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTopicResolver implements TopicResolver {

    @Override
    public String resolve(String eventType) {

        return switch (eventType) {
            case "DepartmentAvailabilityUpdatedEvent" ->
                    "department.availability.updated";

            case "StaffShiftAssignedEvent" ->
                    "staff.shift.assigned";

            case "StaffLeaveAppliedEvent" ->
                    "staff.leave.applied";

            default ->
                    "schedule.events";
        };
    }
}