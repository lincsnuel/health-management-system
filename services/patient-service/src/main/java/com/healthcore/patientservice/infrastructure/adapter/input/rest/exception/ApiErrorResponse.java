package com.healthcore.patientservice.infrastructure.adapter.input.rest.exception;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {}