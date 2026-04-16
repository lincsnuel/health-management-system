package com.healthcore.workforceservice.staff.application.command.validator;

public interface CommandValidator<T> {
    void validate(T command);
}