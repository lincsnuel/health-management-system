package com.healthcore.patientservice.application.exception;

import com.healthcore.healthcorecommon.exception.base.ConflictException;

public class DuplicatePatientEmailException extends ConflictException {

    public DuplicatePatientEmailException(String email) {
        super("A patient with email '" + email + "' already exists in this hospital.");
    }

}