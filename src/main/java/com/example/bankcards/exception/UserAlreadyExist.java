package com.example.bankcards.exception;

public class UserAlreadyExist extends AbstractCustomException {
    public UserAlreadyExist(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public UserAlreadyExist(String message) {
        super(message);
    }
}
