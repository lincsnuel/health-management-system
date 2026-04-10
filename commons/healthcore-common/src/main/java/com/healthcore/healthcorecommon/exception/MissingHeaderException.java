package com.healthcore.healthcorecommon.exception;

import com.healthcore.healthcorecommon.exception.base.InvalidOperationException;

public class MissingHeaderException extends InvalidOperationException {
    public MissingHeaderException(String message) {
        super(message);
    }
}
