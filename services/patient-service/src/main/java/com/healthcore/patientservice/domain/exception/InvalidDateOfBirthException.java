package com.healthcore.patientservice.domain.exception;

import java.awt.dnd.InvalidDnDOperationException;

public class InvalidDateOfBirthException extends InvalidDnDOperationException {
    public InvalidDateOfBirthException(String message) {
        super(message);
    }
}
