package com.healthcore.patientservice.application.exception;

public class PatientNotFoundException extends ApplicationException {
    public PatientNotFoundException(String message) {
        super(message);
    }
}
