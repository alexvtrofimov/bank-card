package com.example.bankcards.exception;

public class CardNotFoundException extends AbstractCustomException {

    public CardNotFoundException(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public CardNotFoundException(String message) {
        super(message);
    }
}
