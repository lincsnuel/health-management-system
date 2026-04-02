package com.healthcore.healthcorecommon.exception.base;

public abstract class NotFoundException extends RuntimeException {
    protected NotFoundException(String message) { super(message); }
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

