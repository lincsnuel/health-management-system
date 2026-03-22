package com.healthcore.healthcorecommon.exception.base;

public abstract class ConflictException extends RuntimeException {
    protected ConflictException(String message) { super(message); }
}
