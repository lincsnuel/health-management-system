package com.healthcore.workforceservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidStaffStateException extends InvalidOperationException {
    public InvalidStaffStateException(String message) {
        super(message);
    }
}