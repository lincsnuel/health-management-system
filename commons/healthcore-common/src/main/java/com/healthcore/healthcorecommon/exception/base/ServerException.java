package com.healthcore.healthcorecommon.exception.base;

public abstract class ServerException extends RuntimeException {
    protected ServerException(String message) { super(message); }
}
