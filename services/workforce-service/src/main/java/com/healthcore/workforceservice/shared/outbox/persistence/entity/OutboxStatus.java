package com.healthcore.workforceservice.shared.outbox.persistence.entity;

public enum OutboxStatus {
    PENDING,
    PUBLISHED,
    FAILED
}