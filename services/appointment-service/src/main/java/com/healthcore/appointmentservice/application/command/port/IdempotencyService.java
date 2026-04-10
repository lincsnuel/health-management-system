package com.healthcore.appointmentservice.application.command.port;

import java.util.UUID;

public interface IdempotencyService {

    boolean isProcessed(String key);

    void markProcessed(String key, UUID resourceId);

    UUID getResourceId(String key);
}