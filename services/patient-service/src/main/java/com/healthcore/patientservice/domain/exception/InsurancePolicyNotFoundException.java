package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.NotFoundException;

public class InsurancePolicyNotFoundException extends NotFoundException {
    public InsurancePolicyNotFoundException(String message) {
        super(message);
    }
}
