package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class NationalIdentityAlreadyAssignedException extends InvalidOperationException {
    public NationalIdentityAlreadyAssignedException(String message) {
        super(message);
    }
}
