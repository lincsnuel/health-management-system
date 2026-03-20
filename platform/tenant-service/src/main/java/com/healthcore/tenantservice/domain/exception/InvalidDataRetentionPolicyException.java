package com.healthcore.tenantservice.domain.exception;

public class InvalidDataRetentionPolicyException extends DomainException {
    public InvalidDataRetentionPolicyException(String message) {
        super(message);
    }
    public InvalidDataRetentionPolicyException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidDataRetentionPolicyException(Throwable cause) {
        super(cause);
    }
}
