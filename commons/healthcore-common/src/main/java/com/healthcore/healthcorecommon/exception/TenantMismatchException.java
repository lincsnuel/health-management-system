package com.healthcore.healthcorecommon.exception;

import com.healthcore.healthcorecommon.exception.base.ConflictException;

public class TenantMismatchException extends ConflictException {
    public TenantMismatchException(String message) {
        super(message);
    }
}
