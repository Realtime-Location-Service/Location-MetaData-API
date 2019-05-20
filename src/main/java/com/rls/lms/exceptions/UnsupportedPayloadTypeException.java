package com.rls.lms.exceptions;

public class UnsupportedPayloadTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnsupportedPayloadTypeException(String message) {
        super(message);
    }
}
