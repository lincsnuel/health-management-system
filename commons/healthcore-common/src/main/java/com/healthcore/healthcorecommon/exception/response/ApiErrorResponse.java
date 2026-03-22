package com.healthcore.healthcorecommon.exception.response;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {}