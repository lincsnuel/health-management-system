package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class AdultRequiresNextOfKinException extends InvalidOperationException {
    public AdultRequiresNextOfKinException(String message) {
        super(message);
    }
}
