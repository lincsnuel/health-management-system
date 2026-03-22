package com.healthcore.patientservice.domain.exception;

import com.healthcore.healthcorecommon.exception.base.NotFoundException;

public class AddressNotFoundException extends NotFoundException {
    public AddressNotFoundException(String message) {
        super(message);
    }
}
