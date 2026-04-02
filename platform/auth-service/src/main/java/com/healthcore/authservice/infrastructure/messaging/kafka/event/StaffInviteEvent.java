package com.healthcore.authservice.infrastructure.messaging.kafka.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StaffInviteEvent {
    private String email;        // staff email
    private String token;        // registration link token
    private String tenantId;
}