package com.healthcore.appointmentservice.application.command.port;

import java.util.UUID;

public interface BillingServiceClient {

    UUID createInvoice(UUID patientId, UUID appointmentId);
}