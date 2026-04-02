package com.healthcore.workforceservice.application.exception;

import com.healthcore.healthcorecommon.exception.base.ConflictException;

public class DuplicateStaffEmailException extends ConflictException {
    public DuplicateStaffEmailException(String message) {
        super(message);
    }
}
