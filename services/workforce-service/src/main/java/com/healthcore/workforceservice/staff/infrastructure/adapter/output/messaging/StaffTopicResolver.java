package com.healthcore.workforceservice.staff.infrastructure.adapter.output.messaging;

import com.healthcore.workforceservice.shared.outbox.messaging.TopicResolver;
import org.springframework.stereotype.Component;

@Component
public class StaffTopicResolver implements TopicResolver {

    @Override
    public String resolve(String eventType) {

        return switch (eventType) {
            case "StaffCreatedEvent" ->
                    "staff.created";

            case "StaffActivatedEvent" ->
                    "staff.activated";

            case "StaffDepartmentAssignedEvent" ->
                    "staff.department.assigned";

            default ->
                    "staff.events";
        };
    }
}