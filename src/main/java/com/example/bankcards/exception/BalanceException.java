package com.example.bankcards.exception;

public class BalanceException extends AbstractCustomException {

    public BalanceException(String message, String debugMessage) {
        super(message, debugMessage);
    }

    public BalanceException(String message) {
        super(message);
    }
}
