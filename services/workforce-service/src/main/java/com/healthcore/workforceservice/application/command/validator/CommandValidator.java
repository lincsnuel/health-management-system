package com.healthcore.workforceservice.application.command.validator;

public interface CommandValidator<T> {
    void validate(T command);
}