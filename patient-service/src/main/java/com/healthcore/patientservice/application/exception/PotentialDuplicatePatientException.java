package com.healthcore.patientservice.application.exception;

import java.time.LocalDate;

public class PotentialDuplicatePatientException extends ApplicationException {

    public PotentialDuplicatePatientException(String name, LocalDate dob) {
        super("A patient with similar details already exists: " + name + " (" + dob + ")");
    }

}
