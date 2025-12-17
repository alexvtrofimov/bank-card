package com.example.bankcards.exception;

public class PasswordException extends AbstractCustomException {

    public PasswordException(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public PasswordException(String message) {
        super(message);
    }
}
