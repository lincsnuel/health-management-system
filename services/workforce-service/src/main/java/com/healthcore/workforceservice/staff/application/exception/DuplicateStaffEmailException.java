package com.healthcore.workforceservice.staff.application.exception;

import com.healthcore.healthcorecommon.exception.base.ConflictException;

public class DuplicateStaffEmailException extends ConflictException {
    public DuplicateStaffEmailException(String message) {
        super(message);
    }
}
