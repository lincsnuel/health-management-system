package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class MinorRequiresResponsiblePartyException extends InvalidOperationException {
    public MinorRequiresResponsiblePartyException(String message) {
        super(message);
    }
}
