package com.healthcore.tenantservice.domain.exception;

public class FacilityAlreadyConfiguredException extends DomainException {
    public FacilityAlreadyConfiguredException(String message) {
        super(message);
    }
}
