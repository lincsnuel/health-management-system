package com.healthcore.appointmentservice.application.event;

import lombok.Getter;

import java.util.UUID;

@Getter
public class EventMetadata {

    private String source;       // billing-service
    private String correlationId; // trace across saga
    private String idempotencyKey;
    private String producedBy;   // service instance

    public static EventMetadata create() {
        EventMetadata meta = new EventMetadata();
        meta.source = "billing-service";
        meta.correlationId = UUID.randomUUID().toString();
        meta.idempotencyKey = UUID.randomUUID().toString();
        meta.producedBy = "billing-service-instance";
        return meta;
    }

    // getters
}