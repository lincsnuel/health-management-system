package com.healthcore.patientservice.application.exception;

public class DuplicatePatientEmailException extends ApplicationException {

    public DuplicatePatientEmailException(String email) {
        super("A patient with email '" + email + "' already exists in this hospital.");
    }

}