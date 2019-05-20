package com.rls.lms.exceptions;

public class UnsupportedSearchParameter extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnsupportedSearchParameter(String message) {
        super(message);
    }
}
