package com.atoz.error.exception;

public class NoRowsFoundException extends RuntimeException {
    public NoRowsFoundException() {
        super();
    }

    public NoRowsFoundException(String message) {
        super(message);
    }
}