package com.healthcore.patientservice.domain.exception;

import com.healthcore.patientservice.application.exception.ApplicationException;

public class InsurancePolicyNotFoundException extends ApplicationException {
    public InsurancePolicyNotFoundException(String message) {
        super(message);
    }
}
