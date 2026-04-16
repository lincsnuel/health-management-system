package com.healthcore.workforceservice.staff.application.exception;

import com.healthcore.healthcorecommon.exception.base.ConflictException;

import java.time.LocalDate;

public class PotentialDuplicatePatientException extends ConflictException {
    public PotentialDuplicatePatientException(String name, LocalDate dob) {
        super("A staff with similar details already exists: " + name + " (" + dob + ")");
    }
}
