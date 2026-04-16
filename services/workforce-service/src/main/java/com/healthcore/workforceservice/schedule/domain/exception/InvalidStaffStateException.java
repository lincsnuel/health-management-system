package com.healthcore.workforceservice.schedule.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class InvalidStaffStateException extends InvalidOperationException {
    public InvalidStaffStateException(String message) {
        super(message);
    }
}