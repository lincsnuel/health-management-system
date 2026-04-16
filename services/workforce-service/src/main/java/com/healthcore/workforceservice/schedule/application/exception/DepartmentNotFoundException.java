package com.healthcore.workforceservice.schedule.application.exception;

import com.healthcore.healthcorecommon.exception.base.NotFoundException;

public class DepartmentNotFoundException extends NotFoundException {
    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
