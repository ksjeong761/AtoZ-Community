package com.atoz.error;

public class SigninFailedException extends RuntimeException {
    public SigninFailedException() {
        super();
    }

    public SigninFailedException(String message) {
        super(message);
    }
}