package com.healthcore.healthcorecommon.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class NullTenantIdException extends InvalidOperationException {
    public NullTenantIdException(String message) {
        super(message);
    }
}
