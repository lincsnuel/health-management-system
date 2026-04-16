package com.healthcore.workforceservice.schedule.infrastructure.adapter.output.messaging.kafka;

import com.healthcore.workforceservice.schedule.infrastructure.adapter.output.persistence.entity.schedule.OutboxEventEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(OutboxEventEntity event) {

        String topic = resolveTopic(event.getEventType());

        kafkaTemplate.send(
                topic,
                event.getAggregateId().toString(),
                event.getPayload()
        );
    }

    private String resolveTopic(String eventType) {

        return switch (eventType) {
            case "DepartmentAvailabilityUpdatedEvent" ->
                    "department.availability.updated";

            case "StaffShiftAssignedEvent" ->
                    "staff.shift.assigned";

            case "StaffLeaveAppliedEvent" ->
                    "staff.leave.applied";

            default ->
                    "workforce.events";
        };
    }
}