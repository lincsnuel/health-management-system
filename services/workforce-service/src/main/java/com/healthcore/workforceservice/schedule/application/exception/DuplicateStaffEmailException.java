package com.healthcore.workforceservice.schedule.application.exception;

import com.healthcore.healthcorecommon.exception.base.ConflictException;

public class DuplicateStaffEmailException extends ConflictException {
    public DuplicateStaffEmailException(String message) {
        super(message);
    }
}
