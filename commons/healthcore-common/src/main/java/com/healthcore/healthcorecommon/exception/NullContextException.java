package com.healthcore.healthcorecommon.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class NullContextException extends InvalidOperationException {
    public NullContextException(String message) {
        super(message);
    }
}
