package com.healthcore.patientservice.application.command.validator;

public interface CommandValidator<T> {
    void validate(T command);
}