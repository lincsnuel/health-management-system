package com.healthcore.appointmentservice.application.event;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
public class PaymentCompletedEvent {

    private UUID eventId;
    private String eventType;
    private String version;

    private UUID appointmentId;
    private UUID billingId;
    private UUID patientId;
    private UUID tenantId;

    private BigDecimal amount;
    private String currency;

    private Instant occurredAt;

    private EventMetadata metadata;

    public PaymentCompletedEvent() {}

    // ========================
    // FACTORY
    // ========================
    public static PaymentCompletedEvent of(
            UUID appointmentId,
            UUID billingId,
            UUID patientId,
            UUID tenantId,
            BigDecimal amount,
            String currency
    ) {
        PaymentCompletedEvent event = new PaymentCompletedEvent();

        event.eventId = UUID.randomUUID();
        event.eventType = "PaymentCompleted";
        event.version = "v1";

        event.appointmentId = appointmentId;
        event.billingId = billingId;
        event.patientId = patientId;
        event.tenantId = tenantId;

        event.amount = amount;
        event.currency = currency;

        event.occurredAt = Instant.now();
        event.metadata = EventMetadata.create();

        return event;
    }

    // getters
}