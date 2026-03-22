package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class ResponsiblePartyRequiredException extends InvalidOperationException {
    public ResponsiblePartyRequiredException(String message) {
        super(message);
    }
}
