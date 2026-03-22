package com.healthcore.healthcorecommon.exception.base;

public abstract class InvalidOperationException extends RuntimeException {
    protected InvalidOperationException(String message) { super(message); }
    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
