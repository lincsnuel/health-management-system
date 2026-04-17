package com.healthcore.workforceservice.shared.outbox.messaging;

public interface TopicResolver {
    String resolve(String eventType);
}