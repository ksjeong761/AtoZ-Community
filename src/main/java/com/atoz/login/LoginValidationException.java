package com.atoz.login;

public class LoginValidationException extends RuntimeException {
    public LoginValidationException() {
        super();
    }

    public LoginValidationException(String message) {
        super(message);
    }
}
